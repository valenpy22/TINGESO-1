package com.example.preuniversitario.controllers;

import com.example.preuniversitario.entities.PaymentEntity;
import com.example.preuniversitario.entities.StudentEntity;
import com.example.preuniversitario.services.PaymentService;
import com.example.preuniversitario.services.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;

/*
* This class represents a payment controller.
* */
@Controller
@RequestMapping
public class PaymentController {
    @Autowired
    private PaymentService paymentService;

    @PostMapping("/payments-sheet")
    public String calculateDiscounts(Model model){
        ArrayList<PaymentEntity> payments = paymentService.calculateDiscounts();
        model.addAttribute("payments", payments);
        return "payments-sheet";
    }

    @GetMapping("/payments-sheet")
    public String listPayments(Model model){
        ArrayList<PaymentEntity> payments = paymentService.getAllDiscounts();
        model.addAttribute("payments", payments);
        return "payments-sheet";
    }
}
