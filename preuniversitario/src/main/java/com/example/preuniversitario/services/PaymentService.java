package com.example.preuniversitario.services;

import com.example.preuniversitario.entities.PaymentEntity;
import com.example.preuniversitario.entities.ReportSummaryEntity;
import com.example.preuniversitario.entities.StudentEntity;
import com.example.preuniversitario.entities.UploadDataEntity;
import com.example.preuniversitario.repositories.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    //THIS HAS TO BE ONE MONTH?
    public ArrayList<PaymentEntity> calculateDiscounts(){
        ArrayList<String> ruts = uploadDataService.getRuts();

        for(String rut : ruts){
            StudentEntity student = studentService.findByRut(rut);

            double discount_school_type = reportSummaryService.calculateDiscountBySchoolType(student.getSchool_type());
            double discount_senior_year = reportSummaryService.calculateDiscountBySeniorYear(student.getSenior_year());
            double discount_average_score = reportSummaryService.calculateDiscountByAverageScore(rut);
            double interest_months_late = reportSummaryService.calculateInterestByMonthsLate(rut);
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

    }

}
