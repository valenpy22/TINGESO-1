package com.example.preuniversitario.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "payments")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class PaymentEntity {
    @Id
    @Column(nullable = false)
    private String rut;

    private double discount_school_type;
    private double discount_senior_year;
    private double discount_average_score;
    private double interest_months_late;
    private double total_price;
}
