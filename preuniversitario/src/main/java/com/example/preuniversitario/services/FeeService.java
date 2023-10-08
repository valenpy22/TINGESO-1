package com.example.preuniversitario.services;

import com.example.preuniversitario.entities.FeeEntity;
import com.example.preuniversitario.entities.ReportSummaryEntity;
import com.example.preuniversitario.repositories.FeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
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

    public void saveFee(String rut, String state, double price, String max_date_payment, int fee_count){
        FeeEntity feeEntity = new FeeEntity();
        feeEntity.setRut(rut);
        feeEntity.setState(state);
        feeEntity.setPrice(price);
        feeEntity.setMax_date_payment(max_date_payment);
        feeEntity.setNumber_of_fee(fee_count);
        feeRepository.save(feeEntity);
    }

    public void saveFee(String rut, int number_of_fee, String payment_date){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String date_now = LocalDate.now().format(formatter);
        int month = LocalDate.now().getMonthValue();
        int year = LocalDate.now().getYear();
        LocalDate max_date;
        LocalDate min_date;

        if(month < 10){
            max_date = LocalDate.parse("10/0"+month+"/"+year, formatter);
            min_date = LocalDate.parse("04/0"+month+"/"+year, formatter);
        }else{
            max_date = LocalDate.parse("10/"+month+"/"+year, formatter);
            min_date = LocalDate.parse("04/"+month+"/"+year, formatter);
        }

        LocalDate datenow = LocalDate.parse(date_now, formatter);
        FeeEntity fee = feeRepository.findByRutAndNumber_of_fee(rut, number_of_fee);

        if(datenow.isBefore(max_date) && datenow.isAfter(min_date)){
            fee.setState("PAID");
            fee.setPayment_date(payment_date);
            feeRepository.save(fee);
        }else{
            fee.setState("PENDING");
        }
    }

    public void save(FeeEntity fee){
        feeRepository.save(fee);
    }

    public ArrayList<FeeEntity> findFees(String rut){
        return (ArrayList<FeeEntity>) feeRepository.filterFeesByRut(rut);
    }

    public void deleteFee(FeeEntity fee){
        this.feeRepository.delete(fee);
    }

    public void deleteFees(){
        this.feeRepository.deleteAll();
    }

    public int getCountPaidFees(String rut){
        return feeRepository.countPaidFeesByRut(rut);
    }

    public double getTotalAmountPaid(String rut){
        List<FeeEntity> paid_fees = feeRepository.getPaidFeesByRut(rut);

        double total_amount = 0;

        for(FeeEntity fee : paid_fees){
            System.out.println(fee.getPrice());
            total_amount = total_amount + fee.getPrice();
        }

        return total_amount;
    }

    public String getLastPayment(String rut){
        FeeEntity fee = feeRepository.getFeeByRutOrderByPaymentDateDesc(rut);

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
        int month = LocalDate.now().getMonthValue();
        int year = LocalDate.now().getYear();
        LocalDate max_date;
        LocalDate min_date;

        if(month < 10){
            max_date = LocalDate.parse("11/0"+month+"/"+year, formatter);
            min_date = LocalDate.parse("04/0"+month+"/"+year, formatter);
        }else{
            max_date = LocalDate.parse("11/"+month+"/"+year, formatter);
            min_date = LocalDate.parse("04/"+month+"/"+year, formatter);
        }

        LocalDate datenow = LocalDate.parse(date_now, formatter);
        FeeEntity fee = feeRepository.findById(feeId);

        if(datenow.isBefore(max_date) && datenow.isAfter(min_date)){
            fee.setState("PAID");
            fee.setPayment_date(date_now);
        }else{
            fee.setState("PENDING");
        }
        feeRepository.save(fee);
    }

}
