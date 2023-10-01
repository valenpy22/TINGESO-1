package com.example.preuniversitario.repositories;

import com.example.preuniversitario.entities.ReportSummaryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ReportSummaryRepository extends JpaRepository<ReportSummaryEntity, String> {
    @Query(value = "SELECT * FROM reports WHERE reports.rut = :rut", nativeQuery = true)
    ReportSummaryEntity findByRut(@Param("rut") String rut);
}
