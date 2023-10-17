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

/*
* This class represents a fee service.
* */
@Service
public class FeeService {
    @Autowired
    FeeRepository feeRepository;

    /**
     * This method saves a fee.
     * @param rut
     * @param state
     * @param price
     * @param max_date_payment
     * @param fee_count
     * */
    public void saveFee(String rut, String state, double price, String max_date_payment, int fee_count){
        FeeEntity feeEntity = new FeeEntity();
        feeEntity.setRut(rut);
        feeEntity.setState(state);
        feeEntity.setPrice(price);
        feeEntity.setMax_date_payment(max_date_payment);
        feeEntity.setNumber_of_fee(fee_count);
        feeRepository.save(feeEntity);
    }

    /**
     * This method saves a fee.
     * @param rut
     * @param number_of_fee
     * @param payment_date
     * */
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

    /**
     * This method saves a fee.
     * @param fee
     * */
    public void save(FeeEntity fee){
        feeRepository.save(fee);
    }

    /**
     * This method finds fees of a specific rut.
     * @param rut
     * @return ArrayList<FeeEntity>
     * */
    public ArrayList<FeeEntity> findFees(String rut){
        return (ArrayList<FeeEntity>) feeRepository.filterFeesByRut(rut);
    }

    /**
     * This method deletes a fee.
     * @param fee
     * */
    public void deleteFee(FeeEntity fee){
        this.feeRepository.delete(fee);
    }

    /**
     * This method deletes all the fees.
     * */
    public void deleteFees(){
        this.feeRepository.deleteAll();
    }

    /**
     * This method counts all the paid fees of a rut.
     * @param rut
     * @return int
     * */
    public int getCountPaidFees(String rut){
        return feeRepository.countPaidFeesByRut(rut);
    }

    /**
     * This method calculates all the paid money by a rut.
     * @param rut
     * @return double
     * */
    public double getTotalAmountPaid(String rut){
        List<FeeEntity> paid_fees = feeRepository.getPaidFeesByRut(rut);

        double total_amount = 0;

        for(FeeEntity fee : paid_fees){
            System.out.println(fee.getPrice());
            total_amount = total_amount + fee.getPrice();
        }

        return total_amount;
    }

    /**
     * This method gets the last payment date.
     * @param rut
     * @return String
     * */
    public String getLastPayment(String rut){
        FeeEntity fee = feeRepository.getFeeByRutOrderByPaymentDateDesc(rut);

        return fee.getPayment_date();
    }

    /**
     * This method gets all the fees.
     * @return ArrayList<FeeEntity>
     * */
    public ArrayList<FeeEntity> getAllFees(){
        return (ArrayList<FeeEntity>) feeRepository.findAll();
    }


    /**
     * This method pays a fee by a feeId.
     * @param feeId
     * */
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
            fee.setState(fee.getState());
        }
        feeRepository.save(fee);
    }

}
