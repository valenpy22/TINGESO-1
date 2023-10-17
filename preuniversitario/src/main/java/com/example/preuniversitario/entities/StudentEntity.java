package com.example.preuniversitario.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/*
* This class represents a student
* */
@Entity
@Table(name = "students")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class StudentEntity {
    @Id
    @Column(unique = true, nullable = false)
    private String rut;
    private String names;
    private String surnames;
    private String birthday;
    private String school_type;
    private String school_name;
    private int senior_year;
}
