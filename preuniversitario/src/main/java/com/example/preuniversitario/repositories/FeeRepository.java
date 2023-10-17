package com.example.preuniversitario.repositories;

import com.example.preuniversitario.entities.FeeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/*
* This interface represents a fee repository.
* */
@Repository
public interface FeeRepository extends JpaRepository<FeeEntity, String> {
    FeeEntity findById(@Param("id") Long id);

    @Query(value = "SELECT * FROM fees WHERE fees.rut = :rut", nativeQuery = true)
    List<FeeEntity> filterFeesByRut(@Param("rut") String rut);

    @Query(value = "SELECT COUNT(*) FROM fees WHERE fees.rut = :rut AND fees.state = 'PAID'", nativeQuery = true)
    int countPaidFeesByRut(@Param("rut") String rut);

    @Query(value = "SELECT * FROM fees WHERE fees.rut = :rut AND fees.state = 'PAID'", nativeQuery = true)
    List<FeeEntity> getPaidFeesByRut(String rut);

    @Query(value = "SELECT * FROM fees WHERE fees.rut = :rut AND fees.state = 'PAID' ORDER BY fees.payment_date DESC LIMIT 1", nativeQuery = true)
    FeeEntity getFeeByRutOrderByPaymentDateDesc(String rut);

    @Query(value = "SELECT * FROM fees WHERE fees.rut = :rut AND fees.number_of_fee = :number_of_fee", nativeQuery = true)
    FeeEntity findByRutAndNumber_of_fee(@Param("rut") String rut, @Param("number_of_fee") int number_of_fee);

}
