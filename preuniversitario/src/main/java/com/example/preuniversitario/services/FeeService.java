package com.example.preuniversitario.services;

import com.example.preuniversitario.entities.FeeEntity;
import com.example.preuniversitario.repositories.FeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;
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

    public List<FeeEntity> findFees(String rut){
        return this.feeRepository.searchFees(rut);
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

}
