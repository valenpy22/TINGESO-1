package com.example.preuniversitario.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


/*
* This class represents a fee.
* */
@Entity
@Table(name = "fees")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class FeeEntity {
    @Id
    @Column(nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(nullable = false)
    private String rut;
    private String max_date_payment;
    private String state;
    private String payment_date;
    private double price;
    private int number_of_fee;
}
