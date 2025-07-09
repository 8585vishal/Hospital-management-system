package com.hospital.controller;

import com.hospital.entity.Patient;
import com.hospital.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/patients")
@CrossOrigin(origins = "*")
public class PatientController {
    
    @Autowired
    private PatientService patientService;
    
    @GetMapping
    public ResponseEntity<List<Patient>> getAllPatients() {
        List<Patient> patients = patientService.getAllPatients();
        return ResponseEntity.ok(patients);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Patient> getPatientById(@PathVariable Long id) {
        Optional<Patient> patient = patientService.getPatientById(id);
        return patient.map(ResponseEntity::ok)
                     .orElse(ResponseEntity.notFound().build());
    }
    
    @PostMapping
    public ResponseEntity<Patient> createPatient(@Valid @RequestBody Patient patient) {
        try {
            Patient savedPatient = patientService.savePatient(patient);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedPatient);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Patient> updatePatient(@PathVariable Long id, @Valid @RequestBody Patient patientDetails) {
        try {
            Patient updatedPatient = patientService.updatePatient(id, patientDetails);
            return ResponseEntity.ok(updatedPatient);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePatient(@PathVariable Long id) {
        try {
            patientService.deletePatient(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @GetMapping("/search")
    public ResponseEntity<List<Patient>> searchPatients(@RequestParam String query) {
        List<Patient> patients = patientService.searchPatients(query);
        return ResponseEntity.ok(patients);
    }
    
    @GetMapping("/count")
    public ResponseEntity<Long> getTotalPatientCount() {
        Long count = patientService.getTotalPatientCount();
        return ResponseEntity.ok(count);
    }
    
    @GetMapping("/gender/{gender}")
    public ResponseEntity<List<Patient>> getPatientsByGender(@PathVariable String gender) {
        List<Patient> patients = patientService.getPatientsByGender(gender);
        return ResponseEntity.ok(patients);
    }
    
    @GetMapping("/email/{email}")
    public ResponseEntity<Patient> getPatientByEmail(@PathVariable String email) {
        Optional<Patient> patient = patientService.findByEmail(email);
        return patient.map(ResponseEntity::ok)
                     .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/phone/{phone}")
    public ResponseEntity<Patient> getPatientByPhone(@PathVariable String phone) {
        Optional<Patient> patient = patientService.findByPhone(phone);
        return patient.map(ResponseEntity::ok)
                     .orElse(ResponseEntity.notFound().build());
    }
}