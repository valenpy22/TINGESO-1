package com.example.preuniversitario.services;

import com.example.preuniversitario.entities.FeeEntity;
import com.example.preuniversitario.entities.ReportSummaryEntity;
import com.example.preuniversitario.entities.StudentEntity;
import com.example.preuniversitario.entities.UploadDataEntity;
import com.example.preuniversitario.repositories.FeeRepository;
import com.example.preuniversitario.repositories.ReportSummaryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    ReportSummaryRepository reportSummaryRepository;

    @Autowired
    UploadDataService uploadDataService;

    @Autowired
    StudentService studentService;

    @Autowired
    FeeRepository feeRepository;

    @Autowired
    FeeService feeService;

    public ArrayList<ReportSummaryEntity> getReportsSummary() {
        reportSummaryRepository.deleteAll();
        List<String> listRuts = uploadDataService.getRuts();

        for(String rut : listRuts){
            reportSummaryRepository.save(calculateSheet(rut));
        }

        return (ArrayList<ReportSummaryEntity>) reportSummaryRepository.findAll();
    }

    public ReportSummaryEntity calculateSheet(String rut){
        ReportSummaryEntity reportSummary = reportSummaryRepository.findByRut(rut);

        return reportSummary;
    }

    public void setPaymentMethodAndFinalPrice(String rut, String number_of_fees, ReportSummaryEntity reportSummary){
        if(number_of_fees.equals("0")){
            reportSummary.setPayment_method("Contado");
            reportSummary.setFinal_price(1500000*0.5);
        }else{
            reportSummary.setPayment_method("Cuotas");
            reportSummary.setFinal_price(1500000);
        }
    }

    public void setMaxFees(String rut, String fees, ReportSummaryEntity reportSummary){
        StudentEntity student = studentService.findByRut(rut);

        int number_of_fees = Integer.parseInt(fees);
        switch (student.getSchool_type()) {
            case "Municipal" -> {
                reportSummary.setTotal_fees(Math.min(number_of_fees, 10));
            }
            case "Subvencionado" -> {
                reportSummary.setTotal_fees(Math.min(number_of_fees, 7));
            }
            case "Privado" -> {
                reportSummary.setTotal_fees(Math.min(number_of_fees, 4));
            }
        }
    }

    public void calculateFinalPriceByDiscount(String rut, ReportSummaryEntity reportSummary){
        StudentEntity student = studentService.findByRut(rut);
        if(reportSummary.getPayment_method().equals("Cuotas")){
            double discount_school_type = calculateDiscountBySchoolType(student.getSchool_type());
            double discount_senior_year = calculateDiscountBySeniorYear(student.getSenior_year());
            double total_price = reportSummary.getFinal_price() - discount_senior_year - discount_school_type;
            reportSummary.setFinal_price(total_price);
        }
        reportSummaryRepository.save(reportSummary);
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

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        LocalDate dBefore = LocalDate.parse("31/12/" + senior_year, formatter);
        LocalDate dAfter = LocalDate.now();

        long diff = dBefore.until(dAfter, ChronoUnit.YEARS);

        double total_discount = 1500000;

        if(diff < 1){
            total_discount = total_discount * 0.15;
        }else if(diff <= 2){
            total_discount = total_discount * 0.08;
        }else if(diff <= 4){
            total_discount = total_discount * 0.04;
        }

        return total_discount;
    }

    public double getFeePrice(String rut, ReportSummaryEntity reportSummary){
        double final_price = reportSummary.getFinal_price();
        int real_number_of_fees = reportSummary.getTotal_fees();
        return final_price/real_number_of_fees;
    }

    public int getMonth(){
        Date date = new Date();
        ZoneId timeZone = ZoneId.systemDefault();

        return date.toInstant().atZone(timeZone).getMonthValue();
    }

    public int getYear(){
        Date date = new Date();
        ZoneId timeZone = ZoneId.systemDefault();

        return date.toInstant().atZone(timeZone).getYear();
    }

    public void generateFees(String rut, String number_of_fees){
        StudentEntity student = studentService.findByRut(rut);
        ReportSummaryEntity reportSummary = new ReportSummaryEntity();
        reportSummary.setRut(student.getRut());
        reportSummary.setNames(student.getNames());
        reportSummary.setSurnames(student.getSurnames());

        setPaymentMethodAndFinalPrice(rut, number_of_fees, reportSummary);
        setMaxFees(rut, number_of_fees, reportSummary);
        calculateFinalPriceByDiscount(rut, reportSummary);
        reportSummaryRepository.save(reportSummary);

        int number_fees = reportSummary.getTotal_fees();
        double fee_price = getFeePrice(rut, reportSummary);
        int month = getMonth();
        String local_date = Integer.toString(getYear());

        for(int i = month; i < number_fees+month; i++){
            feeService.saveFee(rut, "PENDING", fee_price, "10/"+(i+1)+"/"+local_date);
        }

        reportSummaryRepository.save(reportSummary);
    }

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

    public double calculateTotalPriceByFees(String rut){
        List<FeeEntity> fees = feeService.findFees(rut);
        double total = 0;

        for(FeeEntity fee : fees){
            total = total + fee.getPrice();
        }

        return total;
    }

    public double calculateTotalPaid(String rut){
        List<FeeEntity> fees = feeService.findFees(rut);
        double total_paid = 0;
        for(FeeEntity fee : fees){
            if(fee.getState().equals("PAID")){
                total_paid = total_paid + fee.getPrice();
            }
        }
        return total_paid;
    }

    public double calculateTotalDebt(String rut){
        List<FeeEntity> fees = feeService.findFees(rut);
        double total_debt = 0;
        for(FeeEntity fee : fees){
            if(!fee.getState().equals("PAID")){
                total_debt = total_debt + fee.getPrice();
            }
        }
        return total_debt;
    }
}
