package com.example.preuniversitario.repositories;

import com.example.preuniversitario.entities.UploadDataEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UploadDataRepository extends CrudRepository<UploadDataEntity, String> {
}
