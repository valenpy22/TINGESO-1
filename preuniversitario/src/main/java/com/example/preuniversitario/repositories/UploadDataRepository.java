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

    @Query(value = "SELECT COUNT(*) FROM data WHERE data.rut = :rut", nativeQuery = true)
    long findExamsByRut(@Param("rut") String rut);

    @Query(value = "SELECT COUNT(*) as count_exams, YEAR(exam_date) AS year, MONTH(exam_date) AS month FROM data WHERE data.rut = :rut AND data.exam_date = :exam_date GROUP BY YEAR(exam_date), MONTH(exam_date)", nativeQuery = true)
    String findExamsByRutByMonth(@Param("rut") String rut, @Param("exam_date") String exam_date);

    @Query(value = "SELECT data.rut, AVG(data.score) AS average_score FROM data WHERE data.rut =:rut GROUP BY rut", nativeQuery = true)
    double getAverageScoreByRut(@Param("rut") String rut);

    @Query(value = "SELECT AVG(data.score) AS average_score FROM data WHERE data.rut = :rut AND data.exam_date = :exam_date GROUP BY data.rut, YEAR(exam_date), MONTH(exam_date)", nativeQuery = true)
    double getAverageScoreByRutAndMonth(@Param("rut") String rut, @Param("exam_date") String exam_date);
}
