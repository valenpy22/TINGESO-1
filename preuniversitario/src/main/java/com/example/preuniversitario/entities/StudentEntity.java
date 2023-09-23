package com.example.preuniversitario.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Table(name = "students")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudentEntity {
    @Id
    @Column(unique = true, nullable = false)
    private String rut;

    private String m_surname;
    private String f_surname;
    private String first_name;
    private String second_name;
    private Date birthday;
    private String school_type;
    private String school_name;
    private int senior_year;
}
