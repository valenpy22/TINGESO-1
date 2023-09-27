package com.example.preuniversitario.services;

import com.example.preuniversitario.entities.FeeEntity;
import com.example.preuniversitario.repositories.FeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Map;

@Service
public class FeeService {
    @Autowired
    private FeeRepository feeRepository;

    public void saveFee(Map request){
        FeeEntity fee = new FeeEntity();
        fee.setRut(request.get("rut").toString());
        fee.setState(request.get("state").toString());
        fee.setPayment_date(request.get("payment_date").toString());
        fee.setMax_date_payment(request.get("max_date_payment").toString());
        this.feeRepository.save(fee);
    }

    public FeeEntity findFee(String rut, String payment_date){
        return this.feeRepository.searchFee(rut, payment_date);
    }

    public void deleteFee(FeeEntity fee){
        this.feeRepository.delete(fee);
    }

    public void deleteFees(){
        this.feeRepository.deleteAll();
    }

    public boolean isFeePaid(FeeEntity fee){
        if(fee.getState().equals("PAID")){
            return true;
        }else{
            return false;
        }
    }

    public boolean isFeeUpToDate(FeeEntity fee){
        if(!isFeePaid(fee)){
            return !fee.getState().equals("NOTPAID");
        }else{
            return true;
        }
    }

    public long calculateMonthsLate(FeeEntity fee){
        String max_date_payment = fee.getMax_date_payment();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        LocalDate max_date = LocalDate.parse(max_date_payment, formatter);
        LocalDate date_now = LocalDate.now();

        //If max_date is greater than actual date, it means there is time to pay
        //So, the payment state is PENDING
        if(max_date.isAfter(date_now) || max_date.isEqual(date_now)){
            fee.setState("PENDING");
            return 0;
        //If max_date is lesser than actual date, it means the payment is late
        //So, the payment state is NOT PAID
        }else if(max_date.isBefore(date_now) && fee.getPayment_date().isEmpty()){
            fee.setState("NOTPAID");
            return date_now.until(max_date, ChronoUnit.MONTHS);
        }else{
            fee.setState("PAID");
            return 0;
        }
    }
}
