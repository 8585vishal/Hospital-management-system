package com.hospital;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.CrossOrigin;

@SpringBootApplication
@CrossOrigin(origins = "*")
public class HospitalManagementApplication {
    public static void main(String[] args) {
        SpringApplication.run(HospitalManagementApplication.class, args);
    }
}