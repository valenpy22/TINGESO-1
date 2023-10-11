package com.example.preuniversitario;

import com.example.preuniversitario.entities.PaymentEntity;
import com.example.preuniversitario.entities.ReportSummaryEntity;
import com.example.preuniversitario.entities.StudentEntity;
import com.example.preuniversitario.entities.UploadDataEntity;
import com.example.preuniversitario.repositories.PaymentRepository;
import com.example.preuniversitario.repositories.ReportSummaryRepository;
import com.example.preuniversitario.repositories.StudentRepository;
import com.example.preuniversitario.repositories.UploadDataRepository;
import com.example.preuniversitario.services.PaymentService;
import com.example.preuniversitario.services.ReportSummaryService;
import com.example.preuniversitario.services.StudentService;
import com.example.preuniversitario.services.UploadDataService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class PaymentTest {
    @Autowired
    PaymentRepository paymentRepository;

    @Autowired
    PaymentService paymentService;

    @Autowired
    UploadDataRepository uploadDataRepository;

    @Autowired
    StudentRepository studentRepository;

    @Autowired
    ReportSummaryRepository reportSummaryRepository;

    @Test
    void calculateDiscountsTest(){
        paymentRepository.deleteAll();
        PaymentEntity payment = new PaymentEntity();
        StudentEntity student = new StudentEntity();
        UploadDataEntity uploadData = new UploadDataEntity();
        ReportSummaryEntity reportSummary = new ReportSummaryEntity();

        student.setRut("21305689-1");
        student.setSchool_type("Municipal");
        student.setSenior_year(2021);
        uploadData.setRut("21305689-1");
        uploadData.setScore("900");
        uploadData.setExam_date("10/10/2023");
        reportSummary.setRut(student.getRut());
        reportSummary.setPayment_method("Cuotas");
        studentRepository.save(student);
        uploadDataRepository.save(uploadData);
        reportSummaryRepository.save(reportSummary);

        payment.setRut("21305689-1");
        payment.setInterest_months_late(10000);
        payment.setDiscount_senior_year(10000);
        payment.setDiscount_school_type(10000);
        payment.setDiscount_average_score(10000);
        payment.setTotal_price(1000000);

        paymentRepository.save(payment);

        ArrayList<PaymentEntity> arrayList = paymentService.calculateDiscounts();

        //CAMBIAR PARA EL DÍA DE LA PRUEBA
        assertNotNull(arrayList);
        paymentRepository.deleteAll();
    }

    @Test
    void getAllDiscountsTest(){
        paymentRepository.deleteAll();
        ArrayList<PaymentEntity> paymentEntities = paymentService.getAllDiscounts();
        assertEquals(new ArrayList<>(), paymentEntities);
        paymentRepository.deleteAll();
    }
}
