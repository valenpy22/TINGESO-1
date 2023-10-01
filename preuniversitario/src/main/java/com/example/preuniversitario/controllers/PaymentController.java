package com.example.preuniversitario.controllers;

import com.example.preuniversitario.entities.PaymentEntity;
import com.example.preuniversitario.entities.StudentEntity;
import com.example.preuniversitario.services.PaymentService;
import com.example.preuniversitario.services.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;

@Controller
@RequestMapping
public class PaymentController {
    @Autowired
    private PaymentService paymentService;

    @GetMapping("/payments-sheet")
    public String listPayments(Model model){
        ArrayList<PaymentEntity> payments = paymentService.calculateDiscounts();
        model.addAttribute("payments", payments);
        return "payments-sheet";
    }
}
