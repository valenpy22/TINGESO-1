package com.example.preuniversitario.entities;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Table(name = "reports")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ReportSummaryEntity {
    @Id
    @Column(nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String rut;
    private String names;
    private String surnames;
    private int exam_number;
    private double average_score;
    private double final_price;
    private String payment_method;
    private int total_fees;
    private int paid_fees;
    private double total_paid;
    private String last_payment;
    private double debt;
    private int late_fees;
}
