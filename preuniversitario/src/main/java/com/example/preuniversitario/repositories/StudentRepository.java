package com.example.preuniversitario.repositories;

import com.example.preuniversitario.entities.StudentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentRepository extends JpaRepository<StudentEntity, String> {

    //It returns just one entity
    @Query(value = "SELECT * FROM students WHERE students.rut = :rut", nativeQuery = true)
    StudentEntity findByRut(@Param("rut") String rut);

}
