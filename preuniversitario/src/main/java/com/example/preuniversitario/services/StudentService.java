package com.example.preuniversitario.services;

import com.example.preuniversitario.entities.StudentEntity;
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

    public StudentEntity saveStudent(StudentEntity student){
        return studentRepository.save(student);
    }

    public Optional<StudentEntity> getByRut(String rut){
        return studentRepository.findById(rut);
    }

    public double getDiscountBySchoolType(StudentEntity student){
        String school_type = student.getSchool_type();

        if(school_type.equals("Municipal")){
            //arancel = arancel*0.8
        }else if(school_type.equals("Subvencionado")){
            //arancel = arancel*0.9
        }
        //return arancel
        return 1.0;
    }

    public double getDiscountBySeniorYear(StudentEntity student){
        int senior_year = student.getSenior_year();
        Date current_time = new Date();
        Calendar calendar = Calendar.getInstance();

        calendar.setTime(current_time);
        int current_year = calendar.get(Calendar.YEAR);

        int years_since_promotion = current_year - senior_year;

        if(years_since_promotion < 1){
            //arancel = arancel*0.85
        }else if(years_since_promotion >= 1 && years_since_promotion <= 2){
            //arancel = arancel*0.92
        }else if(years_since_promotion >= 3 && years_since_promotion <= 4){
            //arancel = arancel*0.96
        }

        //return arancel
        return 1.0;
    }

}
