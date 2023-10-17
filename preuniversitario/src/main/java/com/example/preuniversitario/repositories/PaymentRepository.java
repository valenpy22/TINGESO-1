package com.example.preuniversitario.repositories;

import com.example.preuniversitario.entities.PaymentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/*
* This interface represents a payment repository
* */
@Repository
public interface PaymentRepository extends JpaRepository<PaymentEntity, String> {
    @Query(value = "SELECT * FROM payments WHERE payments.rut = :rut", nativeQuery = true)
    PaymentEntity findByRut(@Param("rut") String rut);
}
