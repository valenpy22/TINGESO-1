package com.example.preuniversitario;

import com.example.preuniversitario.entities.StudentEntity;
import com.example.preuniversitario.repositories.StudentRepository;
import com.example.preuniversitario.services.StudentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class StudentTest {
    @Autowired
    StudentService studentService;

    @Autowired
    StudentRepository studentRepository;

    @Test
    void testGetStudents(){
        StudentEntity student = new StudentEntity();
        student.setRut("21305689-1");
        student.setNames("Valentina Paz");
        student.setSurnames("Campos Olguín");
        student.setBirthday("22/05/2003");
        student.setSchool_type("Subvencionado");
        student.setSchool_name("Colegio Echaurren");
        student.setSenior_year(2020);

        studentRepository.save(student);
        assertNotNull(studentService.getStudents());
        studentRepository.delete(student);
    }

    @Test
    void testFindByRut(){
        StudentEntity student = new StudentEntity();
        student.setRut("21305689-1");
        student.setNames("Valentina Paz");
        student.setSurnames("Campos Olguín");
        student.setBirthday("22/05/2003");
        student.setSchool_type("Subvencionado");
        student.setSchool_name("Colegio Echaurren");
        student.setSenior_year(2020);

        studentRepository.save(student);
        StudentEntity s = studentService.findByRut("21305689-1");
        String rut = s.getRut();

        assertEquals("21305689-1", rut);
    }

    @Test
    void testGetSchoolType(){
        StudentEntity student = new StudentEntity();
        student.setRut("21305689-1");
        student.setNames("Valentina Paz");
        student.setSurnames("Campos Olguín");
        student.setBirthday("22/05/2003");
        student.setSchool_type("Subvencionado");
        student.setSchool_name("Colegio Echaurren");
        student.setSenior_year(2020);

        studentRepository.save(student);
        String school_type = studentService.getSchoolType("21305689-1");

        assertEquals("Subvencionado", school_type);
    }
}
