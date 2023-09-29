package com.example.preuniversitario.controllers;

import com.example.preuniversitario.entities.FeeEntity;
import com.example.preuniversitario.services.FeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
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

    @GetMapping("/list-fees")
    public String listFeesByRut(@RequestParam(name = "rut", required = false) String rut, Model model){
        if(rut != null && !rut.isEmpty()){
            List<FeeEntity> feesByRut = feeService.findFees(rut);
            model.addAttribute("feesByRut", feesByRut);
        }
        return "list-fees";
    }

    @PostMapping("/save-fee")
    public String saveFee(@RequestParam Map<String, String> allParameters){
        feeService.saveFee(allParameters);
        return "redirect:/saveFee";
    }
}
