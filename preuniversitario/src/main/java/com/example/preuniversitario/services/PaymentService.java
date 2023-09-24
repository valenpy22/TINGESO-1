package com.example.preuniversitario.services;

import com.example.preuniversitario.entities.PaymentEntity;
import com.example.preuniversitario.repositories.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class PaymentService {
    @Autowired
    private PaymentRepository paymentRepository;

    public void savePayment(Map request){
        PaymentEntity payment = new PaymentEntity();
        payment.setRut(request.get("rut").toString());
        payment.setPayment_date(request.get("payment_date").toString());
        this.paymentRepository.save(payment);
    }

    public PaymentEntity findPayment(String rut, String payment_date){
        return this.paymentRepository.findPaymentByRut(rut, payment_date);
    }

    public void deletePayment(PaymentEntity payment){
        this.paymentRepository.delete(payment);
    }

    public void deletePayments(){
        this.paymentRepository.deleteAll();
    }
}
