package com.example.preuniversitario.repositories;

import com.example.preuniversitario.entities.StudentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentRepository extends JpaRepository<StudentEntity, String> {

    @Query(value = "SELECT * FROM students WHERE students.rut = :rut", nativeQuery = true)
    StudentEntity findByRut(@Param("rut") String rut);

    @Query(value = "SELECT students.school_type FROM students WHERE students.rut = :rut", nativeQuery = true)
    String findSchoolType(@Param("rut") String rut);

    @Query(value = "SELECT students.senior_year FROM students WHERE students.rut = :rut", nativeQuery = true)
    int findSeniorYear(@Param("rut") String rut);

}
