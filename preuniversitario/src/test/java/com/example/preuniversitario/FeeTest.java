package com.example.preuniversitario;

import com.example.preuniversitario.entities.FeeEntity;
import com.example.preuniversitario.repositories.FeeRepository;
import com.example.preuniversitario.services.FeeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class FeeTest {
    @Autowired
    FeeService feeService;

    @Autowired
    FeeRepository feeRepository;

    @Test
    void saveFeeTest(){
        FeeEntity fee = new FeeEntity();
        fee.setRut("21305689-1");
        fee.setState("PENDING");
        fee.setPrice(15000);
        fee.setMax_date_payment("10/10/2023");
        fee.setNumber_of_fee(1);
        feeService.saveFee(fee.getRut(), fee.getState(), fee.getPrice(),
                fee.getMax_date_payment(), fee.getNumber_of_fee());

        assertNotNull(feeService.getAllFees());
        feeService.deleteFees();
    }

    /*
    @Test
    void saveFeeDateTest(){
        FeeEntity fee = new FeeEntity();
        fee.setRut("21305689-1");
        fee.setState("PENDING");
        fee.setPrice(15000);
        fee.setMax_date_payment("10/10/2023");
        fee.setNumber_of_fee(1);
        feeService.saveFee(fee.getRut(), fee.getNumber_of_fee(), fee.getPayment_date());
        assertEquals(f);
    }

     */

    @Test
    void saveTest(){
        FeeEntity fee = new FeeEntity();
        fee.setRut("21305689-1");
        fee.setState("PENDING");
        fee.setPrice(15000);
        fee.setMax_date_payment("10/10/2023");
        fee.setNumber_of_fee(1);

        feeService.save(fee);
        assertNotNull(feeService.getAllFees());
        feeService.deleteFees();
    }

    @Test
    void findFeesTest(){
        FeeEntity fee = new FeeEntity();
        fee.setRut("21305689-1");
        fee.setState("PENDING");
        fee.setPrice(15000);
        fee.setMax_date_payment("10/10/2023");
        fee.setNumber_of_fee(1);

        feeService.save(fee);
        assertNotNull(feeService.getAllFees());
        feeService.deleteFee(fee);
    }

    @Test
    void deleteFeeTest(){
        FeeEntity fee = new FeeEntity();
        fee.setRut("21305689-1");
        fee.setState("PENDING");
        fee.setPrice(15000);
        fee.setMax_date_payment("10/10/2023");
        fee.setNumber_of_fee(1);

        feeService.save(fee);
        feeService.deleteFee(fee);
        assertEquals(new ArrayList<>(), feeService.getAllFees());
    }

    @Test
    void deleteFeesTest(){
        FeeEntity fee = new FeeEntity();
        fee.setRut("21305689-1");
        fee.setState("PENDING");
        fee.setPrice(15000);
        fee.setMax_date_payment("10/10/2023");
        fee.setNumber_of_fee(1);

        feeService.save(fee);
        feeService.deleteFees();
        assertEquals(new ArrayList<>(), feeService.getAllFees());
    }

    @Test
    void countPaidFeesTest(){
        FeeEntity fee = new FeeEntity();
        fee.setRut("21305689-1");
        fee.setState("PAID");
        fee.setPrice(15000);
        fee.setMax_date_payment("10/10/2023");
        fee.setNumber_of_fee(1);

        feeService.save(fee);
        int paid_fees = feeService.getCountPaidFees(fee.getRut());
        assertEquals(1, paid_fees, 0);
        feeService.deleteFees();
    }

    @Test
    void totalAmountPaidTest(){
        FeeEntity fee1 = new FeeEntity();
        FeeEntity fee2 = new FeeEntity();

        fee1.setRut("21305689-1");
        fee1.setState("PAID");
        fee1.setPrice(15000);
        fee1.setMax_date_payment("10/10/2023");
        fee1.setNumber_of_fee(1);

        fee2.setRut("21305689-1");
        fee2.setState("NOTPAID");
        fee2.setPrice(15000);
        fee2.setMax_date_payment("10/10/2023");
        fee2.setNumber_of_fee(2);

        feeService.save(fee1);
        feeService.save(fee2);
        assertEquals(15000, feeService.getTotalAmountPaid(fee1.getRut()), 0);
        feeService.deleteFees();
    }

    @Test
    void getLastPaymentTest(){
        FeeEntity fee1 = new FeeEntity();
        FeeEntity fee2 = new FeeEntity();

        fee1.setRut("21305689-1");
        fee1.setState("PAID");
        fee1.setPrice(15000);
        fee1.setMax_date_payment("10/10/2023");
        fee1.setNumber_of_fee(1);
        fee1.setPayment_date("07/10/2023");

        fee2.setRut("21305689-1");
        fee2.setState("PAID");
        fee2.setPrice(15000);
        fee2.setMax_date_payment("10/11/2023");
        fee2.setNumber_of_fee(2);
        fee2.setPayment_date("07/11/2023");

        feeService.save(fee1);
        feeService.save(fee2);

        assertEquals("07/11/2023", feeService.getLastPayment(fee1.getRut()));
        feeService.deleteFees();
    }

    @Test
    void getAllFeesTest(){
        FeeEntity fee1 = new FeeEntity();

        fee1.setRut("21305689-1");
        fee1.setState("PAID");
        fee1.setPrice(15000);
        fee1.setMax_date_payment("10/10/2023");
        fee1.setNumber_of_fee(1);
        fee1.setPayment_date("07/10/2023");
        feeService.save(fee1);

        assertNotNull(feeService.getAllFees());
        feeService.deleteFees();
    }

    /*
    @Test
    void getFeeById(){
        FeeEntity fee1 = new FeeEntity();

        fee1.setRut("21305689-1");
        fee1.setState("PAID");
        fee1.setPrice(15000);
        fee1.setMax_date_payment("10/10/2023");
        fee1.setNumber_of_fee(1);
        fee1.setPayment_date("07/10/2023");
        fee1.setId(123);

        assertNotNull(feeService.getFeeById(123));
    }

     */

    /*
    @Test
    void payFeeTest(){
        FeeEntity fee1 = new FeeEntity();

        fee1.setRut("21305689-1");
        fee1.setState("PAID");
        fee1.setPrice(15000);
        fee1.setMax_date_payment("10/10/2023");
        fee1.setNumber_of_fee(1);
        fee1.setPayment_date("07/10/2023");
        fee1.setId(123);
    }

     */

    @Test
    void deleteAllFeesTest(){
        FeeEntity fee1 = new FeeEntity();

        fee1.setRut("21305689-1");
        fee1.setState("PAID");
        fee1.setPrice(15000);
        fee1.setMax_date_payment("10/10/2023");
        fee1.setNumber_of_fee(1);
        fee1.setPayment_date("07/10/2023");
        feeService.save(fee1);
        feeService.deleteFees();
        assertEquals(new ArrayList<>(), feeService.getAllFees());
    }

}
