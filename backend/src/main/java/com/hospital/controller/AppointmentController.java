package com.hospital.controller;

import com.hospital.entity.Appointment;
import com.hospital.service.AppointmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/appointments")
@CrossOrigin(origins = "*")
public class AppointmentController {
    
    @Autowired
    private AppointmentService appointmentService;
    
    @GetMapping
    public ResponseEntity<List<Appointment>> getAllAppointments() {
        List<Appointment> appointments = appointmentService.getAllAppointments();
        return ResponseEntity.ok(appointments);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Appointment> getAppointmentById(@PathVariable Long id) {
        Optional<Appointment> appointment = appointmentService.getAppointmentById(id);
        return appointment.map(ResponseEntity::ok)
                         .orElse(ResponseEntity.notFound().build());
    }
    
    @PostMapping
    public ResponseEntity<Appointment> createAppointment(@Valid @RequestBody Appointment appointment) {
        try {
            Appointment savedAppointment = appointmentService.saveAppointment(appointment);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedAppointment);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @PostMapping("/create")
    public ResponseEntity<Appointment> createAppointment(
            @RequestParam Long patientId,
            @RequestParam Long doctorId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime appointmentDate,
            @RequestParam String reason) {
        try {
            Appointment appointment = appointmentService.createAppointment(patientId, doctorId, appointmentDate, reason);
            return ResponseEntity.status(HttpStatus.CREATED).body(appointment);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Appointment> updateAppointment(@PathVariable Long id, @Valid @RequestBody Appointment appointmentDetails) {
        try {
            Appointment updatedAppointment = appointmentService.updateAppointment(id, appointmentDetails);
            return ResponseEntity.ok(updatedAppointment);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAppointment(@PathVariable Long id) {
        try {
            appointmentService.deleteAppointment(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<Appointment>> getAppointmentsByPatient(@PathVariable Long patientId) {
        try {
            List<Appointment> appointments = appointmentService.getAppointmentsByPatient(patientId);
            return ResponseEntity.ok(appointments);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @GetMapping("/doctor/{doctorId}")
    public ResponseEntity<List<Appointment>> getAppointmentsByDoctor(@PathVariable Long doctorId) {
        try {
            List<Appointment> appointments = appointmentService.getAppointmentsByDoctor(doctorId);
            return ResponseEntity.ok(appointments);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @GetMapping("/status/{status}")
    public ResponseEntity<List<Appointment>> getAppointmentsByStatus(@PathVariable Appointment.AppointmentStatus status) {
        List<Appointment> appointments = appointmentService.getAppointmentsByStatus(status);
        return ResponseEntity.ok(appointments);
    }
    
    @GetMapping("/today")
    public ResponseEntity<List<Appointment>> getTodayAppointments() {
        List<Appointment> appointments = appointmentService.getTodayAppointments();
        return ResponseEntity.ok(appointments);
    }
    
    @GetMapping("/upcoming")
    public ResponseEntity<List<Appointment>> getUpcomingAppointments() {
        List<Appointment> appointments = appointmentService.getUpcomingAppointments();
        return ResponseEntity.ok(appointments);
    }
    
    @GetMapping("/today/count")
    public ResponseEntity<Long> getTodayAppointmentCount() {
        Long count = appointmentService.getTodayAppointmentCount();
        return ResponseEntity.ok(count);
    }
    
    @GetMapping("/status/{status}/count")
    public ResponseEntity<Long> getAppointmentCountByStatus(@PathVariable Appointment.AppointmentStatus status) {
        Long count = appointmentService.getAppointmentCountByStatus(status);
        return ResponseEntity.ok(count);
    }
    
    @PutMapping("/{id}/status")
    public ResponseEntity<Appointment> updateAppointmentStatus(@PathVariable Long id, @RequestParam Appointment.AppointmentStatus status) {
        try {
            Appointment updatedAppointment = appointmentService.updateAppointmentStatus(id, status);
            return ResponseEntity.ok(updatedAppointment);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @GetMapping("/date-range")
    public ResponseEntity<List<Appointment>> getAppointmentsByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
        List<Appointment> appointments = appointmentService.getAppointmentsByDateRange(start, end);
        return ResponseEntity.ok(appointments);
    }
}