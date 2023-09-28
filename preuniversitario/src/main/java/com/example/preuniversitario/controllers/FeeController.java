package com.example.preuniversitario.controllers;

import com.example.preuniversitario.services.FeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping
public class FeeController {
    @Autowired
    private FeeService feeService;

    @GetMapping("/uploadFee")
    public String newFee(){
        return "uploadFee";
    }
}
