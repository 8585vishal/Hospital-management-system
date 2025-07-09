package com.hospital.service;

import com.hospital.entity.Patient;
import com.hospital.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PatientService {
    
    @Autowired
    private PatientRepository patientRepository;
    
    public List<Patient> getAllPatients() {
        return patientRepository.findAll();
    }
    
    public Optional<Patient> getPatientById(Long id) {
        return patientRepository.findById(id);
    }
    
    public Patient savePatient(Patient patient) {
        return patientRepository.save(patient);
    }
    
    public Patient updatePatient(Long id, Patient patientDetails) {
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Patient not found with id: " + id));
        
        patient.setFirstName(patientDetails.getFirstName());
        patient.setLastName(patientDetails.getLastName());
        patient.setEmail(patientDetails.getEmail());
        patient.setPhone(patientDetails.getPhone());
        patient.setDateOfBirth(patientDetails.getDateOfBirth());
        patient.setGender(patientDetails.getGender());
        patient.setAddress(patientDetails.getAddress());
        patient.setEmergencyContact(patientDetails.getEmergencyContact());
        patient.setMedicalHistory(patientDetails.getMedicalHistory());
        
        return patientRepository.save(patient);
    }
    
    public void deletePatient(Long id) {
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Patient not found with id: " + id));
        patientRepository.delete(patient);
    }
    
    public List<Patient> searchPatients(String search) {
        return patientRepository.searchPatients(search);
    }
    
    public Optional<Patient> findByEmail(String email) {
        return patientRepository.findByEmail(email);
    }
    
    public Optional<Patient> findByPhone(String phone) {
        return patientRepository.findByPhone(phone);
    }
    
    public Long getTotalPatientCount() {
        return patientRepository.countTotalPatients();
    }
    
    public List<Patient> getPatientsByGender(String gender) {
        return patientRepository.findByGender(gender);
    }
    
    public boolean existsByEmail(String email) {
        return patientRepository.findByEmail(email).isPresent();
    }
    
    public boolean existsByPhone(String phone) {
        return patientRepository.findByPhone(phone).isPresent();
    }
}