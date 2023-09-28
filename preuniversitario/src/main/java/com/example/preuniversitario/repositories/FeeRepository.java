package com.example.preuniversitario.repositories;

import com.example.preuniversitario.entities.FeeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FeeRepository extends JpaRepository<FeeEntity, String> {
    @Query(value = "SELECT * FROM fees WHERE fees.rut = :rut",
    nativeQuery = true)
    List<FeeEntity> searchFees(@Param("rut") String rut);

    @Query(value = "SELECT COUNT(*) FROM fees WHERE fees.rut = :rut AND fees.state = 'PAID'", nativeQuery = true)
    long countPaidFeesByRut(@Param("rut") String rut);

    @Query(value = "SELECT * FROM fees WHERE fees.rut = :rut AND fees.state = 'PAID'", nativeQuery = true)
    List<FeeEntity> getPaidFeesByRut(String rut);

    @Query(value = "SELECT * FROM fees WHERE fees.rut = : rut AND fees.state = 'PAID' ORDER BY fees.date_payment DESC", nativeQuery = true)
    FeeEntity findByRutOrderByPaymentDateDesc(String rut);


}
