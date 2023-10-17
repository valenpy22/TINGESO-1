package com.example.preuniversitario.services;

import com.example.preuniversitario.entities.FeeEntity;
import com.example.preuniversitario.entities.StudentEntity;
import com.example.preuniversitario.entities.UploadDataEntity;
import com.example.preuniversitario.repositories.FeeRepository;
import com.example.preuniversitario.repositories.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Optional;

/*
* This class represents a student service.
* */
@Service
public class StudentService {
    @Autowired
    StudentRepository studentRepository;

    @Autowired
    FeeRepository feeRepository;

    /**
     * This method gets all the students.
     * @return ArrayList<StudentEntity>
     * */
    public ArrayList<StudentEntity> getStudents(){
        return (ArrayList<StudentEntity>) studentRepository.findAll();
    }

    /**
     * This method saves a student.
     * @param rut
     * @param names
     * @param surnames
     * @param birthday
     * @param school_type
     * @param school_name
     * @param senior_year
     * */
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

    /**
     * This method finds a student by their rut.
     * @param rut
     * @return StudentEntity
     * */
    public StudentEntity findByRut(String rut){
        return studentRepository.findByRut(rut);
    }

}
