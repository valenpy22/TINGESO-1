package com.example.preuniversitario.repositories;

import com.example.preuniversitario.entities.ReportSummaryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ReportSummaryRepository extends JpaRepository<ReportSummaryEntity, String> {
    public ReportSummaryEntity findByRut(String rut);

    @Query(value = "INSERT INTO payment_summary(rut) values(?)", nativeQuery = true)
    void insertData(@Param("rut") String rut);
}
