package com.example.preuniversitario.services;

import com.example.preuniversitario.entities.ReportSummaryEntity;
import com.example.preuniversitario.entities.StudentEntity;
import com.example.preuniversitario.repositories.ReportSummaryRepository;
import com.example.preuniversitario.repositories.UploadDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
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

    //calculateDiscountByAverageScore

    //calculateInterestByMonthsLate

}
