package com.example.preuniversitario.repositories;

import com.example.preuniversitario.entities.StudentEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentRepository extends CrudRepository<StudentEntity, String> {
    public StudentEntity findByRut(String rut);

    @Query(value = "SELECT * FROM students WHERE students.rut = :rut", nativeQuery = true)
    StudentEntity findByRutNativeQuery(@Param("rut") String rut);
}
