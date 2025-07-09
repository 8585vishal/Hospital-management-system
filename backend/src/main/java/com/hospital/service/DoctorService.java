package com.hospital.service;

import com.hospital.entity.Doctor;
import com.hospital.repository.DoctorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DoctorService {
    
    @Autowired
    private DoctorRepository doctorRepository;
    
    public List<Doctor> getAllDoctors() {
        return doctorRepository.findAll();
    }
    
    public Optional<Doctor> getDoctorById(Long id) {
        return doctorRepository.findById(id);
    }
    
    public Doctor saveDoctor(Doctor doctor) {
        return doctorRepository.save(doctor);
    }
    
    public Doctor updateDoctor(Long id, Doctor doctorDetails) {
        Doctor doctor = doctorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Doctor not found with id: " + id));
        
        doctor.setFirstName(doctorDetails.getFirstName());
        doctor.setLastName(doctorDetails.getLastName());
        doctor.setEmail(doctorDetails.getEmail());
        doctor.setPhone(doctorDetails.getPhone());
        doctor.setSpecialization(doctorDetails.getSpecialization());
        doctor.setLicenseNumber(doctorDetails.getLicenseNumber());
        doctor.setExperienceYears(doctorDetails.getExperienceYears());
        doctor.setQualification(doctorDetails.getQualification());
        doctor.setDepartment(doctorDetails.getDepartment());
        doctor.setConsultationFee(doctorDetails.getConsultationFee());
        doctor.setAvailableDays(doctorDetails.getAvailableDays());
        doctor.setAvailableHours(doctorDetails.getAvailableHours());
        doctor.setIsAvailable(doctorDetails.getIsAvailable());
        
        return doctorRepository.save(doctor);
    }
    
    public void deleteDoctor(Long id) {
        Doctor doctor = doctorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Doctor not found with id: " + id));
        doctorRepository.delete(doctor);
    }
    
    public List<Doctor> searchDoctors(String search) {
        return doctorRepository.searchDoctors(search);
    }
    
    public List<Doctor> getDoctorsBySpecialization(String specialization) {
        return doctorRepository.findBySpecialization(specialization);
    }
    
    public List<Doctor> getDoctorsByDepartment(String department) {
        return doctorRepository.findByDepartment(department);
    }
    
    public List<Doctor> getAvailableDoctors() {
        return doctorRepository.findByIsAvailable(true);
    }
    
    public Long getAvailableDoctorCount() {
        return doctorRepository.countAvailableDoctors();
    }
    
    public List<String> getAllSpecializations() {
        return doctorRepository.findAllSpecializations();
    }
    
    public List<String> getAllDepartments() {
        return doctorRepository.findAllDepartments();
    }
    
    public Optional<Doctor> findByEmail(String email) {
        return doctorRepository.findByEmail(email);
    }
    
    public Optional<Doctor> findByLicenseNumber(String licenseNumber) {
        return doctorRepository.findByLicenseNumber(licenseNumber);
    }
    
    public boolean existsByEmail(String email) {
        return doctorRepository.findByEmail(email).isPresent();
    }
    
    public boolean existsByLicenseNumber(String licenseNumber) {
        return doctorRepository.findByLicenseNumber(licenseNumber).isPresent();
    }
}