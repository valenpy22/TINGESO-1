package com.example.preuniversitario.services;

import com.example.preuniversitario.entities.FeeEntity;
import com.example.preuniversitario.repositories.FeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

}
