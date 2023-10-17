package com.example.preuniversitario.services;

import com.example.preuniversitario.entities.*;
import com.example.preuniversitario.repositories.FeeRepository;
import com.example.preuniversitario.repositories.PaymentRepository;
import com.example.preuniversitario.repositories.ReportSummaryRepository;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/*
* This class represents a report summary service.
* */
@Service
public class ReportSummaryService {
    @Autowired
    ReportSummaryRepository reportSummaryRepository;

    @Autowired
    UploadDataService uploadDataService;

    @Autowired
    StudentService studentService;

    @Autowired
    FeeService feeService;

    @Autowired
    PaymentRepository paymentRepository;

    /**
     * This method gets all the reports summarys.
     * @return ArrayList<ReportSummaryEntity>
     * */
    public ArrayList<ReportSummaryEntity> getReportsSummarys(){
        return (ArrayList<ReportSummaryEntity>) reportSummaryRepository.findAll();
    }

    /**
     * This method calculate all the reports.
     * @return ArrayList<ReportSummaryEntity>
     * */
    public ArrayList<ReportSummaryEntity> calculateAll() {
        List<String> listRuts = uploadDataService.getRuts();

        for(String rut : listRuts){
            calculateSheet(rut);
        }

        return (ArrayList<ReportSummaryEntity>) reportSummaryRepository.findAll();
    }

    /**
     * This method calculates all the discounts by a rut.
     * @param rut
     * */
    public void calculateSheet(String rut){
        ReportSummaryEntity reportSummary = reportSummaryRepository.findByRut(rut);
        reportSummary.setExam_number(calculateNumberOfExams(rut));
        reportSummary.setAverage_score(calculateAverageScore(rut));
        reportSummary.setFinal_price(calculateFinalPriceByDiscount(rut));

        if(reportSummary.getPayment_method().equals("Cuotas")){
            reportSummary.setFinal_price(calculateTotalPriceByFees(rut));
            calculateDiscountByAverageScore(rut);
            calculateInterestByMonthsLate(rut);
        }else{
            reportSummary.setFinal_price(getFinalPrice("0"));
        }

        if(areAnyFeesPaid(rut)){
            reportSummary.setPaid_fees(calculateNumberOfPaidFees(rut));
            reportSummary.setTotal_paid(calculateTotalPaid(rut));
            reportSummary.setLast_payment(feeService.getLastPayment(rut));
        }else{
            reportSummary.setPaid_fees(0);
            if(reportSummary.getPayment_method().equals("Contado")){
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

                String now = LocalDate.now().format(formatter);
                reportSummary.setTotal_paid(750000);
                reportSummary.setLast_payment(now);
            }else{
                reportSummary.setTotal_paid(0);
                reportSummary.setLast_payment("");
            }
        }
        reportSummary.setLate_fees(calculateMonthsLate(rut));
        reportSummary.setDebt(calculateTotalDebt(rut));
        reportSummaryRepository.save(reportSummary);
    }

    /**
     * This method calculates the number of exams of a student.
     * @param rut
     * @return int
     * */
    public int calculateNumberOfExams(String rut){
        return uploadDataService.getNumberOfExamsByRut(rut);
    }

    /**
     * This method gets the payment method by the number of fees.
     * If it equals to 0, it's "Contado".
     * Else, "Cuotas".
     * @param number_of_fees
     * @return String
     * */
    public String getPaymentMethod(String number_of_fees){
        if(number_of_fees.equals("0")){
            return "Contado";
        }else{
            return "Cuotas";
        }
    }

    /**
     * This method gets the final price by the number of fees.
     * @param number_of_fees
     * @return double
     * */
    public double getFinalPrice(String number_of_fees){
        if(number_of_fees.equals("0")){
            return 1500000*0.5;
        }else{
            return 1500000;
        }
    }

    /**
     * This method gets the maximum number of fees by a rut.
     * @param rut
     * @param number_fees
     * @return int
     * */
    public int getTotalFees(String rut, String number_fees){
        StudentEntity student = studentService.findByRut(rut);
        int number_of_fees = Integer.parseInt(number_fees);

        if(student.getSchool_type().equals("Municipal")){
            return Math.min(number_of_fees, 10);
        }else if(student.getSchool_type().equals("Subvencionado")){
            return Math.min(number_of_fees, 7);
        }else{
            return Math.min(number_of_fees, 4);
        }
    }

    /**
     * This method calculates the final price by the discounts made.
     * @param rut
     * @return double
     * */
    public double calculateFinalPriceByDiscount(String rut){
        StudentEntity student = studentService.findByRut(rut);
        ReportSummaryEntity reportSummary = reportSummaryRepository.findByRut(rut);

        if(reportSummary.getPayment_method().equals("Cuotas")){
            double discount_school_type = calculateDiscountBySchoolType(student.getSchool_type());
            double discount_senior_year = calculateDiscountBySeniorYear(student.getSenior_year());
            return reportSummary.getFinal_price() - discount_senior_year - discount_school_type;
        }else{
            return reportSummary.getFinal_price();
        }
    }

    /**
     * This method calculates the discount by the school type.
     * @param school_type
     * @return double
     * */
    public double calculateDiscountBySchoolType(String school_type){
        double total_discount = 0;
        if(school_type.equals("Municipal")){
            total_discount = 1500000 * 0.2;
        }else if(school_type.equals("Subvencionado")){
            total_discount = 1500000 * 0.1;
        }
        return total_discount;
    }

    /**
     * This method calculates the discount by senior year.
     * @param senior_year
     * @return double
     * */
    public double calculateDiscountBySeniorYear(int senior_year){

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        LocalDate dBefore = LocalDate.parse("31/12/" + senior_year, formatter);
        LocalDate dAfter = LocalDate.now();

        long diff = dBefore.until(dAfter, ChronoUnit.YEARS);

        double total_discount = 0;

        if(diff < 1){
            total_discount = 1500000 * 0.15;
        }else if(diff <= 2){
            total_discount = 1500000 * 0.08;
        }else if(diff <= 4){
            total_discount = 1500000 * 0.04;
        }

        return total_discount;
    }

    /**
     * This method get the fee price by the final price
     * and dividing it by the number of fees.
     * @param rut
     * @return double
     * */
    public double getFeePrice(String rut){
        ReportSummaryEntity reportSummary = reportSummaryRepository.findByRut(rut);
        double final_price = reportSummary.getFinal_price();
        int real_number_of_fees = reportSummary.getTotal_fees();
        return final_price/real_number_of_fees;
    }

    /**
     * This method gets the current month.
     * @return int
     * */
    public int getMonth(){
        Date date = new Date();
        ZoneId timeZone = ZoneId.systemDefault();

        return date.toInstant().atZone(timeZone).getMonthValue();
    }

    /**
     * This method gets the current year.
     * @return int
     * */
    public int getYear(){
        Date date = new Date();
        ZoneId timeZone = ZoneId.systemDefault();

        return date.toInstant().atZone(timeZone).getYear();
    }

    /**
     * This method generates the fees by a rut and a number of fees.
     * @param rut
     * @param number_of_fees
     * */
    public void generateFees(String rut, String number_of_fees){
        StudentEntity student = studentService.findByRut(rut);
        ReportSummaryEntity reportSummary = new ReportSummaryEntity();
        reportSummary.setRut(student.getRut());
        reportSummary.setNames(student.getNames());
        reportSummary.setSurnames(student.getSurnames());
        reportSummary.setPayment_method(getPaymentMethod(number_of_fees));

        reportSummaryRepository.save(reportSummary);
        reportSummary.setFinal_price(getFinalPrice(number_of_fees));
        reportSummary.setFinal_price(calculateFinalPriceByDiscount(rut));

        reportSummary.setTotal_fees(getTotalFees(rut, number_of_fees));
        reportSummaryRepository.save(reportSummary);

        int number_fees = reportSummary.getTotal_fees();
        double fee_price = getFeePrice(rut);
        int month = getMonth();

        if(reportSummary.getPayment_method().equals("Contado")){
            reportSummary.setLate_fees(0);
            reportSummary.setDebt(0);
            reportSummary.setPaid_fees(0);
            reportSummary.setTotal_fees(0);
            reportSummary.setLast_payment("");
            reportSummary.setTotal_paid(getFinalPrice(number_of_fees));
            reportSummaryRepository.save(reportSummary);
        }else{
            int fee_count = 1;
            for (int i = month; i < number_fees + month; i++) {
                int currentMonth = (i % 12) + 1;  // It gets the current month (1-12)
                int currentYear = getYear() + (i / 12);  // It gets the current year

                if(currentMonth < 10){
                    feeService.saveFee(rut, "PENDING", fee_price, "10/0" + currentMonth + "/" + currentYear, fee_count);
                }else{
                    feeService.saveFee(rut, "PENDING", fee_price, "10/" + currentMonth + "/" + currentYear, fee_count);
                }
                fee_count++;
            }
        }

    }

    /**
     * This method calculates de discount by average score of the last month.
     * @param rut
     * @return double
     * */
    public double calculateDiscountByAverageScore(String rut){
        String last_date = uploadDataService.getLastExamDate(rut);
        double average_score = uploadDataService.getAverageScoreByRutAndMonth(rut, last_date);
        ArrayList<FeeEntity> fees = feeService.findFees(rut);
        System.out.println(last_date + " " + average_score);

        double discount = 0;
        for(FeeEntity fee : fees){
            if(fee.getState().equals("PENDING")){
                if(average_score >= 950 && average_score <= 1000){
                    discount = fee.getPrice()*0.1;
                    fee.setPrice(fee.getPrice()*0.9);
                }else if(average_score >= 900 && average_score < 950){
                    discount = fee.getPrice()*0.05;
                    fee.setPrice(fee.getPrice()*0.95);
                }else if(average_score >= 850 && average_score < 900){
                    discount = fee.getPrice()*0.02;
                    fee.setPrice(fee.getPrice()*0.98);
                }
                feeService.save(fee);
            }
        }
        System.out.println(discount);
        return discount;
    }

    /**
     * This method calculates how many months are late by a rut.
     * @param rut
     * @return int
     * */
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

    /**
     * This method determines if a fee is late.
     * @param fee
     * @return boolean
     * */
    public boolean isFeeLate(FeeEntity fee){
        if(fee.getPayment_date() == null){
            String max_date_payment = fee.getMax_date_payment();

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

            LocalDate max_date = LocalDate.parse(max_date_payment, formatter);
            LocalDate date_now = LocalDate.now();
            if(max_date.isBefore(date_now) && fee.getState().equals("PENDING")){
                fee.setState("NOTPAID");
                feeService.save(fee);
            }

            return max_date.isBefore(date_now);
        }else return !fee.getState().equals("PAID");
    }

    /**
     * This method calculates the interest based on the months late.
     * @param rut
     * @return double
     * */
    public double calculateInterestByMonthsLate(String rut){
        int months_late = calculateMonthsLate(rut);
        List<FeeEntity> fees = feeService.findFees(rut);
        double interest = 0;

        for(FeeEntity fee : fees){
            if(isFeeLate(fee)){
                if(months_late > 3){
                    interest = fee.getPrice()*0.15;
                    fee.setPrice(fee.getPrice()*1.15);
                }else if(months_late == 3){
                    interest = fee.getPrice()*0.09;
                    fee.setPrice(fee.getPrice()*1.09);
                }else if(months_late == 2){
                    interest = fee.getPrice()*0.06;
                    fee.setPrice(fee.getPrice()*1.06);
                }else if(months_late == 1){
                    interest = fee.getPrice()*0.03;
                    fee.setPrice(fee.getPrice()*1.03);
                }
            }
            feeService.save(fee);
        }
        return interest;
    }

    /**
     * This method calculates the average score of a student
     * @param rut
     * @return double
     * */
    public double calculateAverageScore(String rut){
        return uploadDataService.getAverageScoreByRut(rut);
    }

    /**
     * This method calculates the number of paid fees by a rut.
     * @param rut
     * @return int
     * */
    public int calculateNumberOfPaidFees(String rut){
        return feeService.getCountPaidFees(rut);
    }

    /**
     * This method gets all the reports summaries.
     * @return ArrayList<ReportSummaryEntity>
     * */
    public ArrayList<ReportSummaryEntity> getData(){
        return (ArrayList<ReportSummaryEntity>) reportSummaryRepository.findAll();
    }

    /**
     * This method calculates the total price by the fee prices.
     * @param rut
     * @return double
     * */
    public double calculateTotalPriceByFees(String rut){
        List<FeeEntity> fees = feeService.findFees(rut);
        double total = 0;

        for(FeeEntity fee : fees){
            total = total + fee.getPrice();
        }

        return total;
    }

    /**
     * This method calculates all the money paid.
     * @param rut
     * @return double
     * */
    public double calculateTotalPaid(String rut){
        return feeService.getTotalAmountPaid(rut);
    }

    /**
     * This method calculates the total debt.
     * @param rut
     * @return double
     * */
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

    /**
     * This method gets a report summary by a rut.
     * @param rut
     * @return ReportSummaryEntity
     * */
    public ReportSummaryEntity findByRut(String rut){
        return reportSummaryRepository.findByRut(rut);
    }

    /**
     * This method determines if there are any paid fees of a person by a rut.
     * @param rut
     * @return boolean
     * */
    public boolean areAnyFeesPaid(String rut){
        ArrayList<FeeEntity> fees = feeService.findFees(rut);

        for(FeeEntity fee : fees){
            if(fee.getState().equals("PAID")){
                return true;
            }
        }
        return false;
    }
}
