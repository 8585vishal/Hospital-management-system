package com.hospital.controller;

import com.hospital.entity.Doctor;
import com.hospital.service.DoctorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/doctors")
@CrossOrigin(origins = "*")
public class DoctorController {
    
    @Autowired
    private DoctorService doctorService;
    
    @GetMapping
    public ResponseEntity<List<Doctor>> getAllDoctors() {
        List<Doctor> doctors = doctorService.getAllDoctors();
        return ResponseEntity.ok(doctors);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Doctor> getDoctorById(@PathVariable Long id) {
        Optional<Doctor> doctor = doctorService.getDoctorById(id);
        return doctor.map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
    }
    
    @PostMapping
    public ResponseEntity<Doctor> createDoctor(@Valid @RequestBody Doctor doctor) {
        try {
            Doctor savedDoctor = doctorService.saveDoctor(doctor);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedDoctor);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Doctor> updateDoctor(@PathVariable Long id, @Valid @RequestBody Doctor doctorDetails) {
        try {
            Doctor updatedDoctor = doctorService.updateDoctor(id, doctorDetails);
            return ResponseEntity.ok(updatedDoctor);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDoctor(@PathVariable Long id) {
        try {
            doctorService.deleteDoctor(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @GetMapping("/search")
    public ResponseEntity<List<Doctor>> searchDoctors(@RequestParam String query) {
        List<Doctor> doctors = doctorService.searchDoctors(query);
        return ResponseEntity.ok(doctors);
    }
    
    @GetMapping("/specialization/{specialization}")
    public ResponseEntity<List<Doctor>> getDoctorsBySpecialization(@PathVariable String specialization) {
        List<Doctor> doctors = doctorService.getDoctorsBySpecialization(specialization);
        return ResponseEntity.ok(doctors);
    }
    
    @GetMapping("/department/{department}")
    public ResponseEntity<List<Doctor>> getDoctorsByDepartment(@PathVariable String department) {
        List<Doctor> doctors = doctorService.getDoctorsByDepartment(department);
        return ResponseEntity.ok(doctors);
    }
    
    @GetMapping("/available")
    public ResponseEntity<List<Doctor>> getAvailableDoctors() {
        List<Doctor> doctors = doctorService.getAvailableDoctors();
        return ResponseEntity.ok(doctors);
    }
    
    @GetMapping("/available/count")
    public ResponseEntity<Long> getAvailableDoctorCount() {
        Long count = doctorService.getAvailableDoctorCount();
        return ResponseEntity.ok(count);
    }
    
    @GetMapping("/specializations")
    public ResponseEntity<List<String>> getAllSpecializations() {
        List<String> specializations = doctorService.getAllSpecializations();
        return ResponseEntity.ok(specializations);
    }
    
    @GetMapping("/departments")
    public ResponseEntity<List<String>> getAllDepartments() {
        List<String> departments = doctorService.getAllDepartments();
        return ResponseEntity.ok(departments);
    }
    
    @PutMapping("/{id}/availability")
    public ResponseEntity<Doctor> updateDoctorAvailability(@PathVariable Long id, @RequestParam Boolean available) {
        try {
            Optional<Doctor> doctorOpt = doctorService.getDoctorById(id);
            if (doctorOpt.isPresent()) {
                Doctor doctor = doctorOpt.get();
                doctor.setIsAvailable(available);
                Doctor updatedDoctor = doctorService.saveDoctor(doctor);
                return ResponseEntity.ok(updatedDoctor);
            }
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}