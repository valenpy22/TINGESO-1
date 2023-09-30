package com.example.preuniversitario.services;

import com.example.preuniversitario.entities.FeeEntity;
import com.example.preuniversitario.entities.ReportSummaryEntity;
import com.example.preuniversitario.repositories.FeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class FeeService {
    @Autowired
    FeeRepository feeRepository;

    public void saveFee(String rut, String state, double price, String max_date_payment){
        FeeEntity feeEntity = new FeeEntity();
        feeEntity.setRut(rut);
        feeEntity.setState(state);
        feeEntity.setPrice(price);
        feeEntity.setMax_date_payment(max_date_payment);
        feeRepository.save(feeEntity);
    }

    public ArrayList<FeeEntity> findFees(String rut){
        return (ArrayList<FeeEntity>) feeRepository.searchFees(rut);
    }

    public void deleteFee(FeeEntity fee){
        this.feeRepository.delete(fee);
    }

    public void deleteFees(){
        this.feeRepository.deleteAll();
    }

    public boolean isFeePaid(FeeEntity fee){
        return fee.getState().equals("PAID");
    }

    public boolean isFeeUpToDate(FeeEntity fee){
        if(!isFeePaid(fee)){
            return !fee.getState().equals("NOTPAID");
        }else{
            return true;
        }
    }

    public long getCountPaidFees(String rut){
        return feeRepository.countPaidFeesByRut(rut);
    }

    public double getTotalAmountPaid(String rut){
        List<FeeEntity> paid_fees = feeRepository.getPaidFeesByRut(rut);

        double total_amount = 0;

        for(FeeEntity fee : paid_fees){
            total_amount = total_amount + fee.getPrice();
        }

        return total_amount;
    }

    public double getTotalAmountToPay(String rut){
        List<FeeEntity> fees = feeRepository.searchFees(rut);
        double to_pay = 0;

        for(FeeEntity fee : fees){
            if(!fee.getState().equals("PAID")){
                to_pay = to_pay + fee.getPrice();
            }
        }

        return to_pay;
    }

    public String getLastPayment(String rut){
        FeeEntity fee = feeRepository.findByRutOrderByPaymentDateDesc(rut);

        return fee.getPayment_date();
    }

    public ArrayList<FeeEntity> getAllFees(){
        return (ArrayList<FeeEntity>) feeRepository.findAll();
    }

    public FeeEntity getFeeById(Long feeId) {
        return feeRepository.findById(feeId);
    }

    public void payFee(Long feeId){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        String date_now = LocalDate.now().format(formatter);

        FeeEntity fee = feeRepository.findById(feeId);
        fee.setPayment_date(date_now);
        fee.setState("PAID");

        feeRepository.save(fee);
    }
}
