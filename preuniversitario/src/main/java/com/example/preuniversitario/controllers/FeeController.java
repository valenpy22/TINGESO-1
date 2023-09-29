package com.example.preuniversitario.controllers;

import com.example.preuniversitario.services.FeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@Controller
@RequestMapping
public class FeeController {
    @Autowired
    private FeeService feeService;

    @GetMapping("/upload-fee")
    public String newFee(){
        return "upload-fee";
    }

    @PostMapping("/save-fee")
    public String saveFee(@RequestParam Map<String, String> allParameters){
        feeService.saveFee(allParameters);
        return "redirect:/saveFee";
    }
}
