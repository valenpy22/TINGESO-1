package com.example.preuniversitario.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/*
* This class represents a home controller.
* */
@Controller
@RequestMapping
public class HomeController {
    @GetMapping("/")
    public String main() {
        return "main";
    }
}
