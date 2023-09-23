package com.example.preuniversitario.controllers;

import com.example.preuniversitario.entities.StudentEntity;
import com.example.preuniversitario.services.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;

@Controller
@RequestMapping
public class StudentController {
    @Autowired
    StudentService studentService;

    @GetMapping("/students")
    public String listStudents(Model model){
        ArrayList<StudentEntity> students = studentService.getStudents();
        model.addAttribute("students", students);
        return "index";
    }
}
