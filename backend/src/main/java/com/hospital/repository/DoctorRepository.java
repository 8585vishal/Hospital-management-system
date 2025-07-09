package com.hospital.repository;

import com.hospital.entity.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Long> {
    
    Optional<Doctor> findByEmail(String email);
    
    Optional<Doctor> findByLicenseNumber(String licenseNumber);
    
    List<Doctor> findBySpecialization(String specialization);
    
    List<Doctor> findByDepartment(String department);
    
    List<Doctor> findByIsAvailable(Boolean isAvailable);
    
    @Query("SELECT d FROM Doctor d WHERE " +
           "LOWER(d.firstName) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(d.lastName) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(d.specialization) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(d.department) LIKE LOWER(CONCAT('%', :search, '%'))")
    List<Doctor> searchDoctors(@Param("search") String search);
    
    @Query("SELECT COUNT(d) FROM Doctor d WHERE d.isAvailable = true")
    Long countAvailableDoctors();
    
    @Query("SELECT DISTINCT d.specialization FROM Doctor d ORDER BY d.specialization")
    List<String> findAllSpecializations();
    
    @Query("SELECT DISTINCT d.department FROM Doctor d ORDER BY d.department")
    List<String> findAllDepartments();
}