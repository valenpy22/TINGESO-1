package com.example.preuniversitario.controllers;

import com.example.preuniversitario.entities.FeeEntity;
import com.example.preuniversitario.entities.PaymentEntity;
import com.example.preuniversitario.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

/*
* This class represents a fee controller.
* */
@Controller
@RequestMapping
public class FeeController {
    @Autowired
    FeeService feeService;

    @Autowired
    PaymentService paymentService;

    @Autowired
    ReportSummaryService reportSummaryService;

    @Autowired
    StudentService studentService;

    @Autowired
    UploadDataService uploadDataService;

    @GetMapping("/upload-fee")
    public String newFee(){
        return "upload-fee";
    }

    @GetMapping("/list-fees")
    public String listFeesByRut(@RequestParam(name = "rut", required = false) String rut, Model model){
        ArrayList<FeeEntity> fees;
        if(rut != null && !rut.isEmpty()){
            fees = feeService.findFees(rut);
        }else{
            fees = feeService.getAllFees();
        }
        model.addAttribute("fees", fees);
        return "list-fees";
    }

    @GetMapping("/search-fees")
    public String searchFeesByRut(@RequestParam(name = "rut", required = false) String rut, Model model) {
        ArrayList<FeeEntity> fees;
        if (rut != null && !rut.isEmpty()) {
            fees = feeService.findFees(rut);
        } else {
            fees = feeService.getAllFees();
        }
        model.addAttribute("fees", fees);
        return "list-fees";
    }

    @PostMapping("/pay-fee")
    public String payFee(@RequestParam("feeId") Long feeId, Model model) {

        feeService.payFee(feeId);

        List<FeeEntity> fees = feeService.getAllFees();
        model.addAttribute("fees", fees);

        return "list-fees";
    }


    @GetMapping("/delete-all")
    public String deleteAll(){
        feeService.deleteFees();
        paymentService.deleteAll();
        reportSummaryService.deleteAll();
        studentService.deleteAll();
        uploadDataService.deleteAll();

        return "delete-all";
    }

}
