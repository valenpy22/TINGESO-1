package com.example.preuniversitario.services;

import com.example.preuniversitario.entities.ReportSummaryEntity;
import com.example.preuniversitario.entities.StudentEntity;
import com.example.preuniversitario.repositories.ReportSummaryRepository;
import com.example.preuniversitario.repositories.UploadDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.time.LocalDate;
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

    public int calculateMaxFee(String school_type){
        int max_fee = 4;

        if(school_type.equals("Municipal")){
            max_fee = 10;
        }else if(school_type.equals("Subvencionado")){
            max_fee = 7;
        }

        return max_fee;
    }

    public double getDiscountByAverageScore(int average_score, double fee_price){
        if(average_score >= 950 && average_score <= 1000){
            fee_price = fee_price * 0.9;
        }else if(average_score >= 900 && average_score <= 949){
            fee_price = fee_price * 0.95;
        }else if(average_score >= 850 && average_score <= 899){
            fee_price = fee_price * 0.98;
        }
        return fee_price;
    }

    public double getInterestByLateFee(int months_late){
        double interest = 0;
        if(months_late > 3){
            interest = 0.15;
        }else if(months_late == 3){
            interest = 0.09;
        }else if(months_late == 2){
            interest = 0.06;
        }else if(months_late == 1){
            interest = 0.03;
        }
        return interest;
    }

    public double calculateDiscountBySeniorYear(int senior_year){
        LocalDate current_date = LocalDate.now();

        int year = current_date.getYear();
        int month = current_date.getMonthValue();

        int month_of_promotion = 12;

        return 1.0;


    }

}
