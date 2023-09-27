package com.example.preuniversitario.services;

import com.example.preuniversitario.entities.StudentEntity;
import com.example.preuniversitario.entities.UploadDataEntity;
import com.example.preuniversitario.repositories.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Optional;

@Service
public class StudentService {
    @Autowired
    StudentRepository studentRepository;

    public ArrayList<StudentEntity> getStudents(){
        return (ArrayList<StudentEntity>) studentRepository.findAll();
    }

    public void saveStudent(String rut, String names, String surnames, String birthday,
                            String school_type, String school_name, int senior_year){
        StudentEntity student = new StudentEntity();
        student.setRut(rut);
        student.setNames(names);
        student.setSurnames(surnames);
        student.setBirthday(birthday);
        student.setSchool_type(school_type);
        student.setSchool_name(school_name);
        student.setSenior_year(senior_year);
        studentRepository.save(student);
    }

    public StudentEntity findByRut(String rut){
        return studentRepository.findByRut(rut);
    }

    public String getSchoolType(String rut){
        return studentRepository.findSchoolType(rut);
    }

}
