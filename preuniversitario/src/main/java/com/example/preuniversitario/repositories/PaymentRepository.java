package com.example.preuniversitario.repositories;

import com.example.preuniversitario.entities.PaymentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRepository extends JpaRepository<PaymentEntity, Integer> {
    @Query(value = "SELECT * FROM payments WHERE payments.rut = :rut AND payments.payment_date =:payment_date LIMIT 1",
    nativeQuery = true)
    PaymentEntity findPaymentByRut(@Param("rut") String rut, @Param("payment_date") String payment_date);
}
