package com.example.preuniversitario.repositories;

import com.example.preuniversitario.entities.UploadDataEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UploadDataRepository extends JpaRepository<UploadDataEntity, Integer> {

    @Query(value = "SELECT * FROM data WHERE data.rut = :rut AND data.exam_date = :exam_date LIMIT 1",
    nativeQuery = true)
    UploadDataEntity findData(@Param("rut") String rut, @Param("exam_date") String exam_date);

    @Query(value = "SELECT DISTINCT rut FROM data", nativeQuery = true)
    List<String> findDistinctRut();

    @Query(value = "SELECT data.exam_date FROM data WHERE data.rut = :rut LIMIT 1", nativeQuery = true)
    String findDateByRut(@Param("rut") String rut);

}
