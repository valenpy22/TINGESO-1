package com.example.preuniversitario.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Table(name = "data")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class UploadDataEntity {
    @Id
    @Column(unique = true, nullable = false)
    private String rut;

    private String exam_date;
    private String score;

}
