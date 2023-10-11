package com.example.preuniversitario;

import com.example.preuniversitario.entities.StudentEntity;
import com.example.preuniversitario.services.FeeService;
import com.example.preuniversitario.services.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class PreuniversitarioApplication {
	public static void main(String[] args) {
		SpringApplication.run(PreuniversitarioApplication.class, args);
	}

}
