package com.example.preuniversitario.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/*
* This class represents a payment with discounts.
* */
@Entity
@Table(name = "payments")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class PaymentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String rut;
    private double discount_school_type;
    private double discount_senior_year;
    private double discount_average_score;
    private double interest_months_late;
    private double total_price;
}
