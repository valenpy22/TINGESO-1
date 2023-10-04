package com.example.preuniversitario.services;

import com.example.preuniversitario.entities.*;
import com.example.preuniversitario.repositories.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

@Service
public class PaymentService {
    @Autowired
    PaymentRepository paymentRepository;

    @Autowired
    StudentService studentService;

    @Autowired
    ReportSummaryService reportSummaryService;

    @Autowired
    FeeService feeService;

    @Autowired
    UploadDataService uploadDataService;

    public ArrayList<PaymentEntity> calculateDiscounts(){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String date_now = LocalDate.now().format(formatter);
        int month = LocalDate.now().getMonthValue();
        int year = LocalDate.now().getYear();
        LocalDate max_date;
        LocalDate min_date;

        if(month < 10){
            max_date = LocalDate.parse("10/0"+month+"/"+year, formatter);
            min_date = LocalDate.parse("05/0"+month+"/"+year, formatter);
        }else{
            max_date = LocalDate.parse("10/"+month+"/"+year, formatter);
            min_date = LocalDate.parse("05/"+month+"/"+year, formatter);
        }

        LocalDate datenow = LocalDate.parse(date_now, formatter);
        ArrayList<String> ruts = uploadDataService.getRuts();

        if(datenow.isBefore(min_date) || datenow.isAfter(max_date)){
            for(String rut : ruts){
                StudentEntity student = studentService.findByRut(rut);

                double discount_school_type = reportSummaryService.calculateDiscountBySchoolType(student.getSchool_type());
                double discount_senior_year = reportSummaryService.calculateDiscountBySeniorYear(student.getSenior_year());
                double interest_months_late = reportSummaryService.calculateInterestByMonthsLate(rut);
                double discount_average_score = reportSummaryService.calculateDiscountByAverageScore(rut);
                double total_price = reportSummaryService.calculateTotalPriceByFees(rut);

                PaymentEntity payment = new PaymentEntity();
                payment.setRut(rut);
                payment.setDiscount_school_type(discount_school_type);
                payment.setDiscount_senior_year(discount_senior_year);
                payment.setDiscount_average_score(discount_average_score);
                payment.setInterest_months_late(interest_months_late);
                payment.setTotal_price(total_price);
                paymentRepository.save(payment);
            }

            return (ArrayList<PaymentEntity>) paymentRepository.findAll();
        }else{
            return null;
        }
    }

    public ArrayList<PaymentEntity> getAllDiscounts(){
        return (ArrayList<PaymentEntity>) paymentRepository.findAll();
    }

}
