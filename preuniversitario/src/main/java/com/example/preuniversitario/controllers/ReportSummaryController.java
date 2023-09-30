package com.example.preuniversitario.controllers;

import com.example.preuniversitario.entities.ReportSummaryEntity;
import com.example.preuniversitario.services.FeeService;
import com.example.preuniversitario.services.ReportSummaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping
public class ReportSummaryController {
    @Autowired
    ReportSummaryService reportSummaryService;

    @Autowired
    FeeService feeService;

    @GetMapping("/report-summary")
    public String listReportSummary(Model model){
        ArrayList<ReportSummaryEntity> reportSummary = reportSummaryService.getReportsSummary();

        //ArrayList<ReportSummaryEntity> reportSummary = reportSummaryService.getData();
        model.addAttribute("reportSummary", reportSummary);
        return "report-summary";
    }

    @GetMapping("/generate-fees")
    public String fees(){
        return "generate-fees";
    }

    @PostMapping("/generate-fees")
    public String generateFee(@RequestParam("rut") String rut, @RequestParam("number_of_fees") String number_of_fees){
        reportSummaryService.generateFees(rut, number_of_fees);
        return "redirect:/generate-fees";
    }
}
