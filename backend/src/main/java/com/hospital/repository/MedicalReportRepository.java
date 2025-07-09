package com.hospital.repository;

import com.hospital.entity.MedicalReport;
import com.hospital.entity.Patient;
import com.hospital.entity.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface MedicalReportRepository extends JpaRepository<MedicalReport, Long> {
    
    List<MedicalReport> findByPatient(Patient patient);
    
    List<MedicalReport> findByDoctor(Doctor doctor);
    
    List<MedicalReport> findByReportType(String reportType);
    
    List<MedicalReport> findByPatientOrderByReportDateDesc(Patient patient);
    
    @Query("SELECT mr FROM MedicalReport mr WHERE mr.reportDate BETWEEN :start AND :end ORDER BY mr.reportDate DESC")
    List<MedicalReport> findByReportDateBetween(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);
    
    @Query("SELECT COUNT(mr) FROM MedicalReport mr WHERE mr.patient = :patient")
    Long countByPatient(@Param("patient") Patient patient);
    
    @Query("SELECT DISTINCT mr.reportType FROM MedicalReport mr ORDER BY mr.reportType")
    List<String> findAllReportTypes();
    
    @Query("SELECT mr FROM MedicalReport mr WHERE " +
           "LOWER(mr.title) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(mr.description) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(mr.reportType) LIKE LOWER(CONCAT('%', :search, '%'))")
    List<MedicalReport> searchReports(@Param("search") String search);
}