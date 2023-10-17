package com.example.preuniversitario.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/*
* This class represents an exam
* */
@Entity
@Table(name = "data")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class UploadDataEntity {
    @Id
    @Column(unique = true, nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String rut;
    private String exam_date;
    private String score;
}
