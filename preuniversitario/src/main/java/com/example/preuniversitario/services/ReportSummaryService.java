package com.example.preuniversitario.services;

import com.example.preuniversitario.entities.FeeEntity;
import com.example.preuniversitario.entities.ReportSummaryEntity;
import com.example.preuniversitario.entities.StudentEntity;
import com.example.preuniversitario.entities.UploadDataEntity;
import com.example.preuniversitario.repositories.ReportSummaryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ReportSummaryService {
    @Autowired
    private ReportSummaryRepository reportSummaryRepository;

    @Autowired
    private UploadDataService uploadDataService;

    @Autowired
    private StudentService studentService;

    @Autowired
    private FeeService feeService;

    public void getReportSummary() throws ParseException {
        reportSummaryRepository.deleteAll();
        List<String> listRuts = uploadDataService.getRuts();
        for(String rut : listRuts){
            //calculateSheet
        }
    }

    public void calculateSheet(String rut) throws ParseException {
        StudentEntity current_student = studentService.findByRut(rut);
        ReportSummaryEntity reportSummary = new ReportSummaryEntity();
        reportSummary.setRut(current_student.getRut());
        reportSummary.setNames(current_student.getNames());
        reportSummary.setSurnames(current_student.getSurnames());
        //number of exams
        //average score
        //total price
        //payment method
        reportSummary.setTotal_fees(calculateMaxFee(current_student.getSchool_type()));
        //total paid
        //last date paid
        //to pay
        //number of late fees

    }

    public double calculateDiscountBySchoolType(String school_type){
        double total_discount = 1500000;
        if(school_type.equals("Municipal")){
            total_discount = 1500000 * 0.2;
        }else if(school_type.equals("Subvencionado")){
            total_discount = 1500000 * 0.1;
        }
        return total_discount;
    }

    public double calculateDiscountBySeniorYear(int senior_year){

        LocalDate dBefore = LocalDate.parse("31/12/" + Integer.toString(senior_year), DateTimeFormatter.ISO_DATE);
        LocalDate dAfter = LocalDate.now();

        long diff = dBefore.until(dAfter, ChronoUnit.YEARS);

        double total_discount = 1500000;

        if(diff < 1){
            total_discount = total_discount * 0.15;
        }else if(diff >= 1 && diff <= 2){
            total_discount = total_discount * 0.08;
        }else if(diff >= 3 && diff <= 4){
            total_discount = total_discount * 0.04;
        }

        return total_discount;

    }

    public int calculateMaxFee(String school_type){
        int max_fees = 4;

        if(school_type.equals("Municipal")){
            max_fees = 10;
        }else if(school_type.equals("Subvencionado")){
            max_fees = 7;
        }

        return max_fees;
    }

    /*
    public double calculateInterestByMonthsLate(int months_late, FeeEntity fee){
        double interest = 0;
        if(fee.getState().equals("PENDING")){
            if(months_late > 3){
                interest = fee.getPrice()*1.15;
                fee.setPrice(interest);
            }else if(months_late == 3){
                interest = fee.getPrice()*1.09;
                fee.setPrice(interest);
            }else if(months_late == 2){
                interest = fee.getPrice()*1.06;
                fee.setPrice(interest);
            }else if(months_late == 1){
                interest = fee.getPrice()*1.03;
                fee.setPrice(interest);
            }
        }
        return interest;
    }

     */

    public void calculateDiscountByAverageScore(UploadDataEntity uploadData, FeeEntity fee){
        double average_score = this.uploadDataService.getAverageScoreByRutAndMonth(uploadData.getRut(), uploadData.getExam_date());

        if(average_score >= 950 && average_score <= 1000){
            fee.setPrice(fee.getPrice()*0.9);
        }else if(average_score >= 900 && average_score <= 949){
            fee.setPrice(fee.getPrice()*0.95);
        }else if(average_score >= 850 && average_score <= 899){
            fee.setPrice(fee.getPrice()*0.98);
        }
    }

    public int calculateMonthsLate(String rut){
        List<FeeEntity> fees = feeService.findFees(rut);

        int count = 0;
        for(FeeEntity fee : fees){
            if(isFeeLate(fee)){
                count++;
            }
        }
        return count;
    }

    public boolean isFeeLate(FeeEntity fee){
        String max_date_payment = fee.getMax_date_payment();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        LocalDate max_date = LocalDate.parse(max_date_payment, formatter);
        LocalDate date_now = LocalDate.now();

        return fee.getPayment_date().isEmpty() && max_date.isBefore(date_now);
    }

    public void calculateInterestByMonthsLate(String rut){
        int months_late = calculateMonthsLate(rut);
        List<FeeEntity> fees = feeService.findFees(rut);

        for(FeeEntity fee : fees){
            if(isFeeLate(fee) && fee.getState().equals("PENDING")){
                if(months_late > 3){
                    fee.setPrice(fee.getPrice()*1.15);
                }else if(months_late == 3){
                    fee.setPrice(fee.getPrice()*1.09);
                }else if(months_late == 2){
                    fee.setPrice(fee.getPrice()*1.06);
                }else if(months_late == 1){
                    fee.setPrice(fee.getPrice()*1.03);
                }
            }
        }
    }

    public double calculateAverageScore(String rut){
        return uploadDataService.getAverageScoreByRut(rut);
    }

    public long calculateNumberOfExams(String rut){
        return uploadDataService.getNumberOfExamsByRut(rut);
    }

    public long calculateNumberOfPaidFees(String rut){
        return feeService.getCountPaidFees(rut);
    }

    public ArrayList<ReportSummaryEntity> getData(){
        return (ArrayList<ReportSummaryEntity>) reportSummaryRepository.findAll();
    }

    public void generateFees(String rut, int number_of_fees){
        StudentEntity student = studentService.findByRut(rut);
        ReportSummaryEntity reportSummary = new ReportSummaryEntity();
        reportSummary.setRut(student.getRut());
        reportSummary.setNames(student.getNames());
        reportSummary.setSurnames(student.getSurnames());

        switch (student.getSchool_type()) {
            case "Municipal" -> {
                if(number_of_fees <= 10){
                    reportSummary.setTotal_fees(number_of_fees);
                }
            }
            case "Subvencionado" -> {
                if(number_of_fees <= 7){
                    reportSummary.setTotal_fees(number_of_fees);
                }
            }
            case "Privado" -> {
                if(number_of_fees <= 4){
                    reportSummary.setTotal_fees(number_of_fees);
                }
            }
        }
        if(number_of_fees == 0){
            reportSummary.setPayment_method("Contado");
            reportSummary.setFinal_price(1500000*0.5);
        }else{
            reportSummary.setPayment_method("Cuotas");
            reportSummary.setFinal_price(1500000);
        }
    }

    public double calculateFinalPriceByDiscount(String rut){
        StudentEntity student = studentService.findByRut(rut);
        ReportSummaryEntity reportSummary = reportSummaryRepository.findByRut(rut);
        if(reportSummary.getPayment_method().equals("Cuotas")){
            double discount_school_type = calculateDiscountBySchoolType(student.getSchool_type());
            double discount_senior_year = calculateDiscountBySeniorYear(student.getSenior_year());
            double total_price = reportSummary.getFinal_price() - discount_senior_year - discount_school_type;
            reportSummary.setFinal_price(total_price);
            return total_price;
        }else{
            return 0.0;
        }
    }

    public double calculateFeePriceByTotalPrice(String rut){
        ReportSummaryEntity reportSummary = reportSummaryRepository.findByRut(rut);
        double final_price = reportSummary.getFinal_price();
        int number_of_fees = reportSummary.getTotal_fees();

        double fee_price = final_price/number_of_fees;
        Date date = new Date();
        ZoneId timeZone = ZoneId.systemDefault();
        int year = date.toInstant().atZone(timeZone).getYear();
        int month = date.toInstant().atZone(timeZone).getMonthValue();
        String local_date = Integer.toString(year);

        for(int i = month; i < number_of_fees; i++){
            FeeEntity fee = new FeeEntity();
            fee.setRut(rut);
            fee.setState("PENDING");
            fee.setPrice(fee_price);
            fee.setMax_date_payment("10/"+(i+1)+"/"+local_date);
        }

        return fee_price;
    }
}
