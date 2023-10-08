package com.example.preuniversitario;

import com.example.preuniversitario.entities.FeeEntity;
import com.example.preuniversitario.entities.ReportSummaryEntity;
import com.example.preuniversitario.entities.StudentEntity;
import com.example.preuniversitario.entities.UploadDataEntity;
import com.example.preuniversitario.repositories.FeeRepository;
import com.example.preuniversitario.repositories.ReportSummaryRepository;
import com.example.preuniversitario.repositories.StudentRepository;
import com.example.preuniversitario.repositories.UploadDataRepository;
import com.example.preuniversitario.services.FeeService;
import com.example.preuniversitario.services.ReportSummaryService;
import com.example.preuniversitario.services.StudentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.lang.reflect.Array;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

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

    @Autowired
    UploadDataRepository uploadDataRepository;

    @Test
    void getReportsSummaryTest(){
        StudentEntity student = new StudentEntity();
        student.setRut("21305689-1");
        student.setNames("Juanito");
        student.setSurnames("Pérez");

        ReportSummaryEntity reportSummary = new ReportSummaryEntity();
        reportSummary.setRut(student.getRut());
        reportSummary.setNames(student.getNames());
        reportSummary.setSurnames(student.getSurnames());
        reportSummaryRepository.save(reportSummary);

        assertNotEquals(new ArrayList<>(), reportSummaryService.getReportsSummary());
        reportSummaryRepository.delete(reportSummary);
    }

    @Test
    void calculateSheetTest(){
        studentRepository.deleteAll();
        reportSummaryRepository.deleteAll();
        feeRepository.deleteAll();

        StudentEntity student = new StudentEntity();
        student.setRut("21305689-1");
        student.setNames("Juanito");
        student.setSurnames("Pérez");
        student.setSchool_type("Municipal");
        student.setSenior_year(2019);
        studentRepository.save(student);

        FeeEntity fee1 = new FeeEntity();
        FeeEntity fee2 = new FeeEntity();
        FeeEntity fee3 = new FeeEntity();
        fee1.setRut("21305689-1");
        fee1.setPayment_date("07/07/2023");
        fee1.setState("PAID");
        fee1.setPrice(10000);
        fee2.setRut("21305689-1");
        fee2.setPayment_date("07/07/2023");
        fee2.setState("PAID");
        fee2.setPrice(10000);
        fee3.setRut("21305689-1");
        fee3.setPayment_date("07/07/2023");
        fee3.setState("NOTPAID");
        fee3.setPrice(10000);
        feeRepository.save(fee1);
        feeRepository.save(fee2);
        feeRepository.save(fee3);

        ReportSummaryEntity reportSummary = new ReportSummaryEntity();
        reportSummary.setRut(student.getRut());
        reportSummary.setNames(student.getNames());
        reportSummary.setSurnames(student.getSurnames());
        reportSummary.setPayment_method("Cuotas");
        reportSummary.setTotal_fees(3);
        reportSummary.setTotal_paid(20000);
        reportSummary.setDebt(10000);
        reportSummary.setFinal_price(30000);
        reportSummaryRepository.save(reportSummary);

        reportSummaryService.calculateSheet(student.getRut());
        assertEquals(30000, reportSummary.getFinal_price());
        assertEquals(20000, reportSummary.getTotal_paid());
        assertEquals(10000, reportSummary.getDebt());
        studentRepository.deleteAll();
        reportSummaryRepository.deleteAll();
        feeRepository.deleteAll();
    }

    @Test
    void getPaymentMethodTest(){
        StudentEntity student = new StudentEntity();
        student.setRut("21305689-1");
        studentRepository.save(student);

        ReportSummaryEntity reportSummary = new ReportSummaryEntity();
        reportSummary.setRut(student.getRut());
        reportSummary.setNames("Juan");
        reportSummary.setSurnames("Perez");
        reportSummary.setPayment_method("Contado");
        reportSummaryRepository.save(reportSummary);

        assertEquals("Contado", reportSummaryService.getPaymentMethod("0"));
        reportSummaryRepository.delete(reportSummary);
        studentRepository.deleteAll();
    }

    @Test
    void getFinalPriceTest(){
        ReportSummaryEntity reportSummary = new ReportSummaryEntity();
        reportSummary.setRut("21305689-1");
        reportSummary.setNames("Juan");
        reportSummary.setSurnames("Perez");
        reportSummary.setPayment_method("Contado");
        reportSummary.setTotal_fees(0);
        reportSummary.setTotal_paid(0);
        reportSummary.setDebt(0);
        reportSummary.setFinal_price(10000);
        reportSummary.setTotal_paid(0);
        reportSummaryRepository.save(reportSummary);

        assertEquals(750000, reportSummaryService.getFinalPrice("0"));
        reportSummaryRepository.delete(reportSummary);

    }

    @Test
    void getTotalFeesTest(){
        StudentEntity student = new StudentEntity();
        student.setRut("21305689-1");
        student.setSchool_type("Privado");
        studentRepository.save(student);

        ReportSummaryEntity reportSummary = new ReportSummaryEntity();
        reportSummary.setRut("21305689-1");
        reportSummary.setNames("Juan");
        reportSummary.setSurnames("Perez");
        reportSummary.setTotal_fees(0);
        reportSummary.setTotal_paid(0);
        reportSummary.setDebt(0);
        reportSummary.setFinal_price(0);
        reportSummary.setTotal_paid(0);
        reportSummaryRepository.save(reportSummary);

        assertEquals(4, reportSummaryService.getTotalFees(reportSummary.getRut(), "9"));
        reportSummaryRepository.delete(reportSummary);
        studentRepository.deleteAll();
    }

    @Test
    void calculateFinalPriceByDiscountTest(){
        reportSummaryRepository.deleteAll();
        studentRepository.deleteAll();
        StudentEntity student = new StudentEntity();
        student.setRut("21305689-1");
        student.setNames("Juan");
        student.setSurnames("Perez");
        student.setBirthday("01/01/2000");
        student.setSchool_type("Municipal");
        student.setSchool_name("Liceo A-1");
        student.setSenior_year(2018);
        studentService.saveStudent(student.getRut(), student.getNames(), student.getSurnames(),
                student.getBirthday(), student.getSchool_type(), student.getSchool_name(),
                student.getSenior_year());

        ReportSummaryEntity reportSummary = new ReportSummaryEntity();
        reportSummary.setRut("21305689-1");
        reportSummary.setNames("Juan");
        reportSummary.setSurnames("Perez");
        reportSummary.setPayment_method("Cuotas");
        reportSummary.setTotal_fees(0);
        reportSummary.setTotal_paid(0);
        reportSummary.setDebt(0);
        reportSummary.setFinal_price(1500000);
        reportSummary.setTotal_paid(0);
        reportSummaryRepository.save(reportSummary);

        assertEquals(1140000, reportSummaryService.calculateFinalPriceByDiscount(reportSummary.getRut()));
        reportSummaryRepository.delete(reportSummary);
    }

    @Test
    void calculateDiscountBySchoolTypeTest(){
        StudentEntity student = new StudentEntity();

        student.setRut("21305689-1");
        student.setNames("Juan");
        student.setSurnames("Perez");
        student.setBirthday("01/01/2000");
        student.setSchool_type("Municipal");
        student.setSchool_name("Liceo A-1");
        student.setSenior_year(2018);
        studentService.saveStudent(student.getRut(), student.getNames(), student.getSurnames(),
                student.getBirthday(), student.getSchool_type(), student.getSchool_name(),
                student.getSenior_year());

        ReportSummaryEntity reportSummary = new ReportSummaryEntity();
        reportSummary.setRut(student.getRut());
        reportSummary.setNames(student.getNames());
        reportSummary.setSurnames(student.getSurnames());
        reportSummaryRepository.save(reportSummary);

        assertEquals(300000, reportSummaryService.calculateDiscountBySchoolType(student.getSchool_type()));
        studentRepository.delete(student);
        reportSummaryRepository.delete(reportSummary);
    }

    @Test
    void calculateDiscountBySeniorYearTest(){
        StudentEntity student = new StudentEntity();

        student.setRut("21305689-1");
        student.setNames("Juan");
        student.setSurnames("Perez");
        student.setBirthday("01/01/2000");
        student.setSchool_type("Municipal");
        student.setSchool_name("Liceo A-1");
        student.setSenior_year(2018);
        studentService.saveStudent(student.getRut(), student.getNames(), student.getSurnames(),
                student.getBirthday(), student.getSchool_type(), student.getSchool_name(),
                student.getSenior_year());

        ReportSummaryEntity reportSummary = new ReportSummaryEntity();
        reportSummary.setRut(student.getRut());
        reportSummary.setNames(student.getNames());
        reportSummary.setSurnames(student.getSurnames());
        reportSummaryRepository.save(reportSummary);

        System.out.println(reportSummaryService.calculateDiscountBySeniorYear(student.getSenior_year()));
        assertEquals(60000, reportSummaryService.calculateDiscountBySeniorYear(student.getSenior_year()));
        studentRepository.delete(student);
        reportSummaryRepository.delete(reportSummary);
    }

    @Test
    void getFeePriceTest(){
        ReportSummaryEntity reportSummary = new ReportSummaryEntity();
        reportSummary.setRut("21305689-1");
        reportSummary.setFinal_price(10000);
        reportSummary.setTotal_fees(1);
        reportSummaryRepository.save(reportSummary);

        FeeEntity fee = new FeeEntity();
        fee.setRut(reportSummary.getRut());
        fee.setPrice(10000);
        feeRepository.save(fee);

        StudentEntity student = new StudentEntity();
        student.setRut("21305689-1");
        student.setNames("Juan");
        student.setSurnames("Perez");
        studentRepository.save(student);

        assertEquals(10000, reportSummaryService.getFeePrice(student.getRut()));
        feeRepository.deleteAll();
        reportSummaryRepository.deleteAll();
        studentRepository.deleteAll();
    }

    @Test
    void getMonthTest(){
        ReportSummaryEntity reportSummary = new ReportSummaryEntity();
        assertEquals(10, reportSummaryService.getMonth());
        reportSummaryRepository.delete(reportSummary);
    }

    @Test
    void getYearTest(){
        ReportSummaryEntity reportSummary = new ReportSummaryEntity();
        assertEquals(2023, reportSummaryService.getYear());
        reportSummaryRepository.delete(reportSummary);
    }

    @Test
    void generateFeesTest(){
        feeRepository.deleteAll();
        studentRepository.deleteAll();
        reportSummaryRepository.deleteAll();
        StudentEntity student = new StudentEntity();
        student.setRut("21305689-1");
        student.setNames("Juan");
        student.setSurnames("Perez");
        student.setBirthday("01/01/2000");
        student.setSchool_type("Municipal");
        student.setSchool_name("Liceo A-1");
        student.setSenior_year(2018);
        studentService.saveStudent(student.getRut(), student.getNames(), student.getSurnames(),
                student.getBirthday(), student.getSchool_type(), student.getSchool_name(),
                student.getSenior_year());

        ReportSummaryEntity reportSummary = new ReportSummaryEntity();

        reportSummaryService.generateFees(student.getRut(), "10");
        assertEquals(10, reportSummaryService.findByRut("21305689-1").getTotal_fees());
        assertNotEquals(new ArrayList<>(), feeRepository.findAll());
        studentRepository.delete(student);
        reportSummaryRepository.delete(reportSummary);
    }

    @Test
    void calculateDiscountByAverageScoreTest(){
        StudentEntity student = new StudentEntity();

        student.setRut("21305689-1");
        student.setNames("Juan");
        student.setSurnames("Perez");
        student.setBirthday("01/01/2000");
        student.setSchool_type("Municipal");
        student.setSchool_name("Liceo A-1");
        student.setSenior_year(2018);
        studentService.saveStudent(student.getRut(), student.getNames(), student.getSurnames(),
                student.getBirthday(), student.getSchool_type(), student.getSchool_name(),
                student.getSenior_year());

        ReportSummaryEntity reportSummary = new ReportSummaryEntity();
        reportSummary.setRut(student.getRut());
        reportSummary.setNames(student.getNames());
        reportSummary.setSurnames(student.getSurnames());
        reportSummary.setTotal_fees(0);
        reportSummary.setPaid_fees(0);
        reportSummary.setLate_fees(0);
        reportSummary.setFinal_price(0);
        reportSummary.setTotal_paid(0);
        reportSummary.setDebt(0);
        reportSummaryRepository.save(reportSummary);

        UploadDataEntity uploadData = new UploadDataEntity();
        UploadDataEntity uploadData2 = new UploadDataEntity();
        uploadData.setRut(student.getRut());
        uploadData.setScore("900");
        uploadData.setExam_date("10/01/2020");
        uploadData2.setRut(student.getRut());
        uploadData2.setScore("1000");
        uploadData2.setExam_date("10/01/2020");
        uploadDataRepository.save(uploadData);
        uploadDataRepository.save(uploadData2);

        FeeEntity fee1 = new FeeEntity();
        FeeEntity fee2 = new FeeEntity();
        fee1.setRut("21305689-1");
        fee1.setState("PENDING");
        fee1.setPrice(100);
        fee2.setRut("21305689-1");
        fee2.setState("PENDING");
        fee2.setPrice(100);
        feeRepository.save(fee1);
        feeRepository.save(fee2);
    
        assertEquals(10, reportSummaryService.calculateDiscountByAverageScore(student.getRut()));
        uploadDataRepository.deleteAll();
        feeRepository.deleteAll();
        studentRepository.deleteAll();
        reportSummaryRepository.deleteAll();
    }

    @Test
    void calculateMonthsLateTest(){
        FeeEntity fee = new FeeEntity();
        fee.setRut("21305689-1");
        fee.setMax_date_payment("06/07/2023");
        fee.setState("NOTPAID");
        feeRepository.save(fee);

        assertEquals(1, reportSummaryService.calculateMonthsLate(fee.getRut()));
        feeRepository.delete(fee);
    }

    @Test
    void isFeeLateTest(){
        FeeEntity fee = new FeeEntity();
        fee.setRut("21305689-1");
        fee.setMax_date_payment("06/07/2023");
        fee.setState("NOTPAID");
        feeRepository.save(fee);

        assertTrue(reportSummaryService.isFeeLate(fee));
        feeRepository.delete(fee);
    }

    @Test
    void calculateInterestByMonthsLateTest(){
        feeRepository.deleteAll();
        reportSummaryRepository.deleteAll();
        studentRepository.deleteAll();
        FeeEntity fee = new FeeEntity();
        fee.setRut("21305689-1");
        fee.setPayment_date("07/07/2023");
        fee.setMax_date_payment("05/07/2023");
        fee.setState("NOTPAID");
        fee.setPrice(100);
        feeRepository.save(fee);

        ReportSummaryEntity reportSummary = new ReportSummaryEntity();
        reportSummary.setRut(fee.getRut());
        reportSummary.setNames("Juan");
        reportSummary.setSurnames("Perez");
        reportSummary.setTotal_fees(3);
        reportSummaryRepository.save(reportSummary);

        assertEquals(3, reportSummaryService.calculateInterestByMonthsLate(fee.getRut()));
        feeRepository.delete(fee);
        reportSummaryRepository.delete(reportSummary);
    }


    @Test
    void calculateAverageScoreTest(){
        StudentEntity student = new StudentEntity();

        student.setRut("21305689-1");
        student.setNames("Juan");
        student.setSurnames("Perez");
        student.setBirthday("01/01/2000");
        student.setSchool_type("Municipal");
        student.setSchool_name("Liceo A-1");
        student.setSenior_year(2018);
        studentService.saveStudent(student.getRut(), student.getNames(), student.getSurnames(),
                student.getBirthday(), student.getSchool_type(), student.getSchool_name(),
                student.getSenior_year());

        ReportSummaryEntity reportSummary = new ReportSummaryEntity();
        reportSummary.setRut(student.getRut());
        reportSummary.setNames(student.getNames());
        reportSummary.setSurnames(student.getSurnames());
        reportSummary.setTotal_fees(0);
        reportSummary.setPaid_fees(0);
        reportSummary.setLate_fees(0);
        reportSummary.setFinal_price(0);
        reportSummary.setTotal_paid(0);
        reportSummary.setDebt(0);
        reportSummaryRepository.save(reportSummary);

        UploadDataEntity uploadData = new UploadDataEntity();
        UploadDataEntity uploadData2 = new UploadDataEntity();
        uploadData.setRut(student.getRut());
        uploadData.setScore("100");
        uploadData.setExam_date("01/01/2020");
        uploadData2.setRut(student.getRut());
        uploadData2.setScore("50");
        uploadData2.setExam_date("10/01/2020");
        uploadDataRepository.save(uploadData);
        uploadDataRepository.save(uploadData2);

        assertEquals(75, reportSummaryService.calculateAverageScore(student.getRut()));
        studentRepository.delete(student);
        reportSummaryRepository.delete(reportSummary);
    }

    @Test
    void calculateNumberOfPaidFeesTest(){
        FeeEntity fee1 = new FeeEntity();
        FeeEntity fee2 = new FeeEntity();
        FeeEntity fee3 = new FeeEntity();
        fee1.setRut("21305689-1");
        fee1.setPayment_date("07/07/2023");
        fee1.setState("PAID");
        fee2.setRut("21305689-1");
        fee2.setPayment_date("07/07/2023");
        fee2.setState("PAID");
        fee3.setRut("21305689-1");
        fee3.setPayment_date("07/07/2023");
        fee3.setState("NOTPAID");
        feeRepository.save(fee1);
        feeRepository.save(fee2);
        feeRepository.save(fee3);

        ReportSummaryEntity reportSummary = new ReportSummaryEntity();
        reportSummary.setRut(fee1.getRut());
        reportSummary.setNames("Juan");
        reportSummary.setSurnames("Perez");
        reportSummary.setTotal_fees(3);
        reportSummary.setPaid_fees(2);
        reportSummary.setLate_fees(1);
        reportSummary.setFinal_price(30000);
        reportSummary.setTotal_paid(20000);
        reportSummary.setDebt(10000);
        reportSummaryRepository.save(reportSummary);

        assertEquals(2, reportSummaryService.calculateNumberOfPaidFees(fee1.getRut()));
        feeRepository.deleteAll();
        reportSummaryRepository.delete(reportSummary);
    }

    @Test
    void getDataTest(){
        StudentEntity student = new StudentEntity();

        student.setRut("21305689-1");
        student.setNames("Juan");
        student.setSurnames("Perez");
        student.setBirthday("01/01/2000");
        student.setSchool_type("Municipal");
        student.setSchool_name("Liceo A-1");
        student.setSenior_year(2018);
        studentService.saveStudent(student.getRut(), student.getNames(), student.getSurnames(),
                student.getBirthday(), student.getSchool_type(), student.getSchool_name(),
                student.getSenior_year());

        ReportSummaryEntity reportSummary = new ReportSummaryEntity();
        reportSummary.setRut(student.getRut());
        reportSummary.setNames(student.getNames());
        reportSummary.setSurnames(student.getSurnames());
        reportSummary.setTotal_fees(0);
        reportSummary.setPaid_fees(0);
        reportSummary.setLate_fees(0);
        reportSummary.setFinal_price(0);
        reportSummary.setTotal_paid(0);
        reportSummary.setDebt(0);
        reportSummaryRepository.save(reportSummary);

        assertNotEquals(new ArrayList<>(), reportSummaryService.getData());
        studentRepository.delete(student);
        reportSummaryRepository.delete(reportSummary);
    }

    @Test
    void calculateTotalPriceByFeesTest(){
        feeRepository.deleteAll();
        FeeEntity fee1 = new FeeEntity();
        FeeEntity fee2 = new FeeEntity();
        FeeEntity fee3 = new FeeEntity();
        fee1.setRut("21305689-1");
        fee1.setPayment_date("07/07/2023");
        fee1.setState("PAID");
        fee1.setPrice(10000);
        fee2.setRut("21305689-1");
        fee2.setPayment_date("07/07/2023");
        fee2.setState("PAID");
        fee2.setPrice(10000);
        fee3.setRut("21305689-1");
        fee3.setPayment_date("07/07/2023");
        fee3.setState("NOTPAID");
        fee3.setPrice(10000);
        feeRepository.save(fee1);
        feeRepository.save(fee2);
        feeRepository.save(fee3);

        ReportSummaryEntity reportSummary = new ReportSummaryEntity();
        reportSummary.setRut(fee1.getRut());
        reportSummary.setNames("Juan");
        reportSummary.setSurnames("Perez");
        reportSummary.setTotal_fees(3);
        reportSummary.setPaid_fees(2);
        reportSummary.setLate_fees(1);
        reportSummary.setFinal_price(30000);
        reportSummary.setTotal_paid(20000);
        reportSummary.setDebt(10000);
        reportSummaryRepository.save(reportSummary);

        assertEquals(30000, reportSummaryService.calculateTotalPriceByFees(fee1.getRut()));
        feeRepository.deleteAll();
        reportSummaryRepository.delete(reportSummary);
    }

    @Test
    void calculateTotalPaidTest(){
        FeeEntity fee1 = new FeeEntity();
        FeeEntity fee2 = new FeeEntity();
        FeeEntity fee3 = new FeeEntity();
        fee1.setRut("21305689-1");
        fee1.setPayment_date("07/07/2023");
        fee1.setState("PAID");
        fee1.setPrice(10000);
        fee2.setRut("21305689-1");
        fee2.setPayment_date("07/07/2023");
        fee2.setState("PAID");
        fee2.setPrice(10000);
        fee3.setRut("21305689-1");
        fee3.setPayment_date("07/07/2023");
        fee3.setState("NOTPAID");
        fee3.setPrice(10000);
        feeRepository.save(fee1);
        feeRepository.save(fee2);
        feeRepository.save(fee3);

        ReportSummaryEntity reportSummary = new ReportSummaryEntity();
        reportSummary.setRut(fee1.getRut());
        reportSummary.setNames("Juan");
        reportSummary.setSurnames("Perez");
        reportSummary.setTotal_fees(3);
        reportSummary.setPaid_fees(2);
        reportSummary.setLate_fees(1);
        reportSummary.setFinal_price(30000);
        reportSummary.setTotal_paid(20000);
        reportSummary.setDebt(10000);
        reportSummaryRepository.save(reportSummary);

        assertEquals(20000, reportSummaryService.calculateTotalPaid(fee1.getRut()));
        feeRepository.deleteAll();
        reportSummaryRepository.delete(reportSummary);
    }

    @Test
    void calculateTotalDebtTest(){
        StudentEntity student = new StudentEntity();
        student.setRut("21305689-1");
        student.setNames("Juanito");
        student.setSurnames("Pérez");

        FeeEntity fee1 = new FeeEntity();
        FeeEntity fee2 = new FeeEntity();
        FeeEntity fee3 = new FeeEntity();
        fee1.setRut("21305689-1");
        fee1.setPayment_date("07/07/2023");
        fee1.setState("PAID");
        fee1.setPrice(10000);
        fee2.setRut("21305689-1");
        fee2.setPayment_date("07/07/2023");
        fee2.setState("PAID");
        fee2.setPrice(10000);
        fee3.setRut("21305689-1");
        fee3.setPayment_date("07/07/2023");
        fee3.setState("NOTPAID");
        fee3.setPrice(10000);
        feeRepository.save(fee1);
        feeRepository.save(fee2);
        feeRepository.save(fee3);

        ReportSummaryEntity reportSummary = new ReportSummaryEntity();
        reportSummary.setRut(student.getRut());
        reportSummary.setNames(student.getNames());
        reportSummary.setSurnames(student.getSurnames());
        reportSummary.setTotal_fees(0);
        reportSummary.setTotal_paid(0);
        reportSummary.setDebt(0);
        reportSummary.setFinal_price(0);
        reportSummary.setTotal_paid(0);
        reportSummaryRepository.save(reportSummary);

        assertEquals(10000, reportSummaryService.calculateTotalDebt(fee1.getRut()));
        feeRepository.deleteAll();
        reportSummaryRepository.delete(reportSummary);
    }

    @Test
    void findByRutTest(){
        StudentEntity student = new StudentEntity();

        student.setRut("86545656-1");
        student.setNames("Juan");
        student.setSurnames("Perez");
        student.setBirthday("01/01/2000");
        student.setSchool_type("Municipal");
        student.setSchool_name("Liceo A-1");
        student.setSenior_year(2018);
        studentService.saveStudent(student.getRut(), student.getNames(), student.getSurnames(),
                student.getBirthday(), student.getSchool_type(), student.getSchool_name(),
                student.getSenior_year());

        ReportSummaryEntity reportSummary = new ReportSummaryEntity();
        reportSummary.setRut(student.getRut());
        reportSummary.setNames(student.getNames());
        reportSummary.setSurnames(student.getSurnames());
        reportSummary.setTotal_fees(0);
        reportSummary.setTotal_paid(0);
        reportSummary.setDebt(0);
        reportSummary.setFinal_price(0);
        reportSummary.setTotal_paid(0);
        reportSummaryRepository.save(reportSummary);

        assertEquals(reportSummary, reportSummaryService.findByRut(student.getRut()));
        studentRepository.delete(student);
        reportSummaryRepository.delete(reportSummary);
    }

    @Test
    void areAnyFeesPaidTest(){
        FeeEntity fee1 = new FeeEntity();
        FeeEntity fee2 = new FeeEntity();
        fee1.setRut("21305689-1");
        fee1.setPayment_date("07/07/2023");
        fee1.setState("PAID");
        fee2.setRut("21305689-1");
        fee2.setPayment_date("07/07/2023");
        fee2.setState("PAID");
        feeRepository.save(fee1);
        feeRepository.save(fee2);
        
        ReportSummaryEntity reportSummary = new ReportSummaryEntity();
        assertTrue(reportSummaryService.areAnyFeesPaid(fee1.getRut()));
        feeRepository.delete(fee1);
        feeRepository.delete(fee2);
        reportSummaryRepository.delete(reportSummary);
    }
}
