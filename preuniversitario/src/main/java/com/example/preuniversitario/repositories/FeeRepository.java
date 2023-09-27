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

}
