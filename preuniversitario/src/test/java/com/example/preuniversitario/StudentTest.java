package com.example.preuniversitario;

import com.example.preuniversitario.entities.StudentEntity;
import com.example.preuniversitario.repositories.StudentRepository;
import com.example.preuniversitario.services.StudentService;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@SpringBootTest
public class StudentTest {
    @Autowired
    StudentService studentService;
    
    @Autowired
    StudentRepository studentRepository;

    @Test
    void saveStudentTest(){
        StudentEntity student = new StudentEntity();

        student.setRut("21305689-1");
        student.setNames("Juan");
        student.setSurnames("Perez");
        student.setBirthday("01/01/2000");
        student.setSchool_type("Municipal");
        student.setSchool_name("Liceo A-1");
        student.setSenior_year(2018);
        studentService.saveStudent(student.getRut(), student.getNames(), student.getSurnames(),
                student.getBirthday(), student.getSchool_type(), student.getSchool_name(),
                student.getSenior_year());

        assertNotEquals(new ArrayList<>(), studentService.getStudents());
        studentRepository.delete(student);
    }

    @Test
    void findByRutTest(){
        StudentEntity student = new StudentEntity();

        student.setRut("21305689-1");
        student.setNames("Juan");
        student.setSurnames("Perez");
        student.setBirthday("01/01/2000");
        student.setSchool_type("Municipal");
        student.setSchool_name("Liceo A-1");
        student.setSenior_year(2018);
        studentService.saveStudent(student.getRut(), student.getNames(), student.getSurnames(),
                student.getBirthday(), student.getSchool_type(), student.getSchool_name(),
                student.getSenior_year());

        assertEquals(student, studentService.findByRut(student.getRut()));
        studentRepository.delete(student);
    }
}
