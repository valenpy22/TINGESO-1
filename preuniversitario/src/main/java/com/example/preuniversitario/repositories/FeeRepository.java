package com.example.preuniversitario.repositories;

import com.example.preuniversitario.entities.FeeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface FeeRepository extends JpaRepository<FeeEntity, String> {
    @Query(value = "SELECT * FROM fees WHERE fees.rut = :rut AND fees.max_date_payment =:max_date_payment LIMIT 1",
    nativeQuery = true)
    FeeEntity searchFee(@Param("rut") String rut, @Param("max_date_payment") String max_date_payment);
}
