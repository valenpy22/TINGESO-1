package com.example.preuniversitario;

import com.example.preuniversitario.entities.FeeEntity;
import com.example.preuniversitario.repositories.FeeRepository;
import com.example.preuniversitario.services.FeeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class FeeTest {
    @Autowired
    FeeService feeService;

    @Autowired
    FeeRepository feeRepository;

    @Test
    void testGetFees(){
        FeeEntity fee = new FeeEntity();
        fee.setRut("21305689-1");
        fee.setPrice(150000);
        fee.setState("PAID");
        fee.setPayment_date("30/09/2023");
        fee.setMax_date_payment("01/10/2023");

        feeRepository.save(fee);
        assertNotNull(feeService.getAllFees());
        feeRepository.deleteAll();
    }
}
