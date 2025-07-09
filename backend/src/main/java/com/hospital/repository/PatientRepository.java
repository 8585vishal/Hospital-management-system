package com.hospital.repository;

import com.hospital.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Long> {
    
    Optional<Patient> findByEmail(String email);
    
    Optional<Patient> findByPhone(String phone);
    
    List<Patient> findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(String firstName, String lastName);
    
    @Query("SELECT p FROM Patient p WHERE " +
           "LOWER(p.firstName) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(p.lastName) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(p.email) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "p.phone LIKE CONCAT('%', :search, '%')")
    List<Patient> searchPatients(@Param("search") String search);
    
    @Query("SELECT COUNT(p) FROM Patient p")
    Long countTotalPatients();
    
    List<Patient> findByGender(String gender);
}