package com.example.preuniversitario;

import com.example.preuniversitario.entities.FeeEntity;
import com.example.preuniversitario.entities.ReportSummaryEntity;
import com.example.preuniversitario.entities.StudentEntity;
import com.example.preuniversitario.repositories.FeeRepository;
import com.example.preuniversitario.repositories.ReportSummaryRepository;
import com.example.preuniversitario.repositories.StudentRepository;
import com.example.preuniversitario.services.FeeService;
import com.example.preuniversitario.services.ReportSummaryService;
import com.example.preuniversitario.services.StudentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.ZoneId;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class ReportSummaryTest {
    @Autowired
    StudentRepository studentRepository;

    @Autowired
    StudentService studentService;

    @Autowired
    ReportSummaryRepository reportSummaryRepository;

    @Autowired
    ReportSummaryService reportSummaryService;

    @Autowired
    FeeRepository feeRepository;

    @Autowired
    FeeService feeService;

    @Test
    void testGenerateFees(){
        StudentEntity student = new StudentEntity();
        ReportSummaryEntity reportSummary = new ReportSummaryEntity();
        student.setRut("21305689-1");
        student.setNames("Valentina Paz");
        student.setSurnames("Campos Olguín");
        student.setBirthday("22/05/2003");
        student.setSchool_type("Subvencionado");
        student.setSchool_name("Colegio Echaurren");
        student.setSenior_year(2020);
        studentRepository.save(student);

        reportSummary.setRut(student.getRut());
        reportSummary.setNames(student.getNames());
        reportSummary.setSurnames(student.getSurnames());
        reportSummary.setTotal_fees(7);
        reportSummary.setPayment_method("Cuotas");
        reportSummary.setFinal_price(1500000);
        reportSummaryRepository.save(reportSummary);

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
            fee.setRut(reportSummary.getRut());
            fee.setState("PENDING");
            fee.setPrice(fee_price);
            fee.setMax_date_payment("10/"+(i+1)+"/"+local_date);
            feeRepository.save(fee);
        }

        String rut = "21305689-1";

        for(FeeEntity fee : feeService.getAllFees()){
            System.out.println(fee.getPrice());
        }

    }
}
