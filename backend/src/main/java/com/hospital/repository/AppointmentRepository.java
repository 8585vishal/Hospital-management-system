package com.hospital.repository;

import com.hospital.entity.Appointment;
import com.hospital.entity.Doctor;
import com.hospital.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    
    List<Appointment> findByPatient(Patient patient);
    
    List<Appointment> findByDoctor(Doctor doctor);
    
    List<Appointment> findByStatus(Appointment.AppointmentStatus status);
    
    List<Appointment> findByAppointmentDateBetween(LocalDateTime start, LocalDateTime end);
    
    @Query("SELECT a FROM Appointment a WHERE a.doctor = :doctor AND DATE(a.appointmentDate) = DATE(:date)")
    List<Appointment> findByDoctorAndDate(@Param("doctor") Doctor doctor, @Param("date") LocalDateTime date);
    
    @Query("SELECT a FROM Appointment a WHERE a.patient = :patient AND a.status IN :statuses ORDER BY a.appointmentDate DESC")
    List<Appointment> findByPatientAndStatuses(@Param("patient") Patient patient, @Param("statuses") List<Appointment.AppointmentStatus> statuses);
    
    @Query("SELECT COUNT(a) FROM Appointment a WHERE a.status = :status")
    Long countByStatus(@Param("status") Appointment.AppointmentStatus status);
    
    @Query("SELECT COUNT(a) FROM Appointment a WHERE DATE(a.appointmentDate) = CURRENT_DATE")
    Long countTodayAppointments();
    
    @Query("SELECT a FROM Appointment a WHERE a.appointmentDate >= :startTime AND a.appointmentDate <= :endTime ORDER BY a.appointmentDate")
    List<Appointment> findUpcomingAppointments(@Param("startTime") LocalDateTime startTime, @Param("endTime") LocalDateTime endTime);
}