package com.example.preuniversitario.controllers;

import com.example.preuniversitario.services.FeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping
public class FeeController {
    @Autowired
    FeeService feeService;
}
