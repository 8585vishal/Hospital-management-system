package com.hospital.service;

import com.hospital.entity.Appointment;
import com.hospital.entity.Doctor;
import com.hospital.entity.Patient;
import com.hospital.repository.AppointmentRepository;
import com.hospital.repository.DoctorRepository;
import com.hospital.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class AppointmentService {
    
    @Autowired
    private AppointmentRepository appointmentRepository;
    
    @Autowired
    private PatientRepository patientRepository;
    
    @Autowired
    private DoctorRepository doctorRepository;
    
    public List<Appointment> getAllAppointments() {
        return appointmentRepository.findAll();
    }
    
    public Optional<Appointment> getAppointmentById(Long id) {
        return appointmentRepository.findById(id);
    }
    
    public Appointment saveAppointment(Appointment appointment) {
        return appointmentRepository.save(appointment);
    }
    
    public Appointment createAppointment(Long patientId, Long doctorId, LocalDateTime appointmentDate, String reason) {
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new RuntimeException("Patient not found with id: " + patientId));
        
        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new RuntimeException("Doctor not found with id: " + doctorId));
        
        Appointment appointment = new Appointment(patient, doctor, appointmentDate, reason);
        return appointmentRepository.save(appointment);
    }
    
    public Appointment updateAppointment(Long id, Appointment appointmentDetails) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Appointment not found with id: " + id));
        
        if (appointmentDetails.getPatient() != null) {
            appointment.setPatient(appointmentDetails.getPatient());
        }
        if (appointmentDetails.getDoctor() != null) {
            appointment.setDoctor(appointmentDetails.getDoctor());
        }
        if (appointmentDetails.getAppointmentDate() != null) {
            appointment.setAppointmentDate(appointmentDetails.getAppointmentDate());
        }
        if (appointmentDetails.getReason() != null) {
            appointment.setReason(appointmentDetails.getReason());
        }
        if (appointmentDetails.getStatus() != null) {
            appointment.setStatus(appointmentDetails.getStatus());
        }
        if (appointmentDetails.getNotes() != null) {
            appointment.setNotes(appointmentDetails.getNotes());
        }
        if (appointmentDetails.getDiagnosis() != null) {
            appointment.setDiagnosis(appointmentDetails.getDiagnosis());
        }
        if (appointmentDetails.getPrescription() != null) {
            appointment.setPrescription(appointmentDetails.getPrescription());
        }
        
        return appointmentRepository.save(appointment);
    }
    
    public void deleteAppointment(Long id) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Appointment not found with id: " + id));
        appointmentRepository.delete(appointment);
    }
    
    public List<Appointment> getAppointmentsByPatient(Long patientId) {
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new RuntimeException("Patient not found with id: " + patientId));
        return appointmentRepository.findByPatient(patient);
    }
    
    public List<Appointment> getAppointmentsByDoctor(Long doctorId) {
        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new RuntimeException("Doctor not found with id: " + doctorId));
        return appointmentRepository.findByDoctor(doctor);
    }
    
    public List<Appointment> getAppointmentsByStatus(Appointment.AppointmentStatus status) {
        return appointmentRepository.findByStatus(status);
    }
    
    public List<Appointment> getAppointmentsByDateRange(LocalDateTime start, LocalDateTime end) {
        return appointmentRepository.findByAppointmentDateBetween(start, end);
    }
    
    public List<Appointment> getTodayAppointments() {
        LocalDateTime startOfDay = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0);
        LocalDateTime endOfDay = LocalDateTime.now().withHour(23).withMinute(59).withSecond(59);
        return appointmentRepository.findByAppointmentDateBetween(startOfDay, endOfDay);
    }
    
    public List<Appointment> getUpcomingAppointments() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime endOfWeek = now.plusDays(7);
        return appointmentRepository.findUpcomingAppointments(now, endOfWeek);
    }
    
    public Long getTodayAppointmentCount() {
        return appointmentRepository.countTodayAppointments();
    }
    
    public Long getAppointmentCountByStatus(Appointment.AppointmentStatus status) {
        return appointmentRepository.countByStatus(status);
    }
    
    public Appointment updateAppointmentStatus(Long id, Appointment.AppointmentStatus status) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Appointment not found with id: " + id));
        appointment.setStatus(status);
        return appointmentRepository.save(appointment);
    }
}