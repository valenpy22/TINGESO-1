package com.example.preuniversitario.controllers;

import com.example.preuniversitario.entities.StudentEntity;
import com.example.preuniversitario.services.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

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

    @GetMapping("/new-student")
    public String student(){
        return "new-student";
    }

    @PostMapping("/new-student")
    public String newStudent(@RequestParam("rut") String rut,
                             @RequestParam("names") String names,
                             @RequestParam("surnames") String surnames,
                             @RequestParam("birthday") String birthday,
                             @RequestParam("school_type") String school_type,
                             @RequestParam("school_name") String school_name,
                             @RequestParam("senior_year") int senior_year){
        studentService.saveStudent(rut, names, surnames, birthday, school_type, school_name, senior_year);
        return "redirect:/new-student";

    }

}
