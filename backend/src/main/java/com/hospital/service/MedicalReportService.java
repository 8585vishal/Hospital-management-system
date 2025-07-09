package com.hospital.service;

import com.hospital.entity.MedicalReport;
import com.hospital.entity.Patient;
import com.hospital.entity.Doctor;
import com.hospital.repository.MedicalReportRepository;
import com.hospital.repository.PatientRepository;
import com.hospital.repository.DoctorRepository;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
public class MedicalReportService {
    
    @Autowired
    private MedicalReportRepository medicalReportRepository;
    
    @Autowired
    private PatientRepository patientRepository;
    
    @Autowired
    private DoctorRepository doctorRepository;
    
    private final Random random = new Random();
    
    public List<MedicalReport> getAllReports() {
        return medicalReportRepository.findAll();
    }
    
    public Optional<MedicalReport> getReportById(Long id) {
        return medicalReportRepository.findById(id);
    }
    
    public MedicalReport saveReport(MedicalReport report) {
        return medicalReportRepository.save(report);
    }
    
    public MedicalReport createReport(Long patientId, Long doctorId, String reportType, String title, String description) {
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new RuntimeException("Patient not found with id: " + patientId));
        
        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new RuntimeException("Doctor not found with id: " + doctorId));
        
        MedicalReport report = new MedicalReport(patient, doctor, reportType, title, description);
        return medicalReportRepository.save(report);
    }
    
    public MedicalReport updateReport(Long id, MedicalReport reportDetails) {
        MedicalReport report = medicalReportRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Medical report not found with id: " + id));
        
        if (reportDetails.getReportType() != null) {
            report.setReportType(reportDetails.getReportType());
        }
        if (reportDetails.getTitle() != null) {
            report.setTitle(reportDetails.getTitle());
        }
        if (reportDetails.getDescription() != null) {
            report.setDescription(reportDetails.getDescription());
        }
        if (reportDetails.getTestResults() != null) {
            report.setTestResults(reportDetails.getTestResults());
        }
        if (reportDetails.getRecommendations() != null) {
            report.setRecommendations(reportDetails.getRecommendations());
        }
        
        return medicalReportRepository.save(report);
    }
    
    public void deleteReport(Long id) {
        MedicalReport report = medicalReportRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Medical report not found with id: " + id));
        medicalReportRepository.delete(report);
    }
    
    public List<MedicalReport> getReportsByPatient(Long patientId) {
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new RuntimeException("Patient not found with id: " + patientId));
        return medicalReportRepository.findByPatientOrderByReportDateDesc(patient);
    }
    
    public List<MedicalReport> getReportsByDoctor(Long doctorId) {
        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new RuntimeException("Doctor not found with id: " + doctorId));
        return medicalReportRepository.findByDoctor(doctor);
    }
    
    public List<MedicalReport> getReportsByType(String reportType) {
        return medicalReportRepository.findByReportType(reportType);
    }
    
    public List<MedicalReport> searchReports(String search) {
        return medicalReportRepository.searchReports(search);
    }
    
    public List<String> getAllReportTypes() {
        return medicalReportRepository.findAllReportTypes();
    }
    
    // Generate random medical reports for demonstration
    public MedicalReport generateRandomReport() {
        List<Patient> patients = patientRepository.findAll();
        List<Doctor> doctors = doctorRepository.findAll();
        
        if (patients.isEmpty() || doctors.isEmpty()) {
            throw new RuntimeException("Need at least one patient and one doctor to generate report");
        }
        
        Patient randomPatient = patients.get(random.nextInt(patients.size()));
        Doctor randomDoctor = doctors.get(random.nextInt(doctors.size()));
        
        String[] reportTypes = {"Blood Test", "X-Ray", "MRI Scan", "CT Scan", "Ultrasound", "ECG", "Biopsy"};
        String[] titles = {"Annual Checkup", "Emergency Assessment", "Follow-up Examination", "Routine Screening", "Diagnostic Test"};
        String[] descriptions = {
            "Patient shows normal vital signs and overall good health condition.",
            "Comprehensive examination reveals no significant abnormalities.",
            "Routine screening completed with standard protocols followed.",
            "Patient reported minor symptoms, examination shows no concerning findings.",
            "Regular checkup indicates patient is in stable condition."
        };
        String[] testResults = {
            "All parameters within normal range",
            "Slight elevation in some markers, requiring monitoring",
            "Results indicate good overall health status",
            "Minor deviations from normal, not clinically significant",
            "Test completed successfully, no abnormal findings"
        };
        String[] recommendations = {
            "Continue current lifestyle and return for next scheduled appointment",
            "Maintain healthy diet and exercise routine",
            "Schedule follow-up in 6 months",
            "No immediate action required, monitor symptoms",
            "Regular checkups recommended annually"
        };
        
        String reportType = reportTypes[random.nextInt(reportTypes.length)];
        String title = titles[random.nextInt(titles.length)];
        String description = descriptions[random.nextInt(descriptions.length)];
        String testResult = testResults[random.nextInt(testResults.length)];
        String recommendation = recommendations[random.nextInt(recommendations.length)];
        
        MedicalReport report = new MedicalReport(randomPatient, randomDoctor, reportType, title, description);
        report.setTestResults(testResult);
        report.setRecommendations(recommendation);
        
        return medicalReportRepository.save(report);
    }
    
    public byte[] generateReportPDF(Long reportId) throws DocumentException, IOException {
        MedicalReport report = medicalReportRepository.findById(reportId)
                .orElseThrow(() -> new RuntimeException("Medical report not found with id: " + reportId));
        
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Document document = new Document();
        PdfWriter.getInstance(document, baos);
        
        document.open();
        
        // Title
        Font titleFont = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD);
        Paragraph title = new Paragraph("MEDICAL REPORT", titleFont);
        title.setAlignment(Element.ALIGN_CENTER);
        title.setSpacingAfter(20);
        document.add(title);
        
        // Report details
        Font headerFont = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD);
        Font normalFont = new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL);
        
        document.add(new Paragraph("Report ID: " + report.getId(), headerFont));
        document.add(new Paragraph("Report Type: " + report.getReportType(), normalFont));
        document.add(new Paragraph("Title: " + report.getTitle(), normalFont));
        document.add(new Paragraph("Date: " + report.getReportDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")), normalFont));
        document.add(new Paragraph(" "));
        
        // Patient details
        document.add(new Paragraph("PATIENT INFORMATION", headerFont));
        document.add(new Paragraph("Name: " + report.getPatient().getFullName(), normalFont));
        document.add(new Paragraph("Email: " + report.getPatient().getEmail(), normalFont));
        document.add(new Paragraph("Phone: " + report.getPatient().getPhone(), normalFont));
        document.add(new Paragraph(" "));
        
        // Doctor details
        document.add(new Paragraph("ATTENDING PHYSICIAN", headerFont));
        document.add(new Paragraph("Name: " + report.getDoctor().getFullName(), normalFont));
        document.add(new Paragraph("Specialization: " + report.getDoctor().getSpecialization(), normalFont));
        document.add(new Paragraph(" "));
        
        // Report content
        document.add(new Paragraph("DESCRIPTION", headerFont));
        document.add(new Paragraph(report.getDescription(), normalFont));
        document.add(new Paragraph(" "));
        
        if (report.getTestResults() != null) {
            document.add(new Paragraph("TEST RESULTS", headerFont));
            document.add(new Paragraph(report.getTestResults(), normalFont));
            document.add(new Paragraph(" "));
        }
        
        if (report.getRecommendations() != null) {
            document.add(new Paragraph("RECOMMENDATIONS", headerFont));
            document.add(new Paragraph(report.getRecommendations(), normalFont));
        }
        
        document.close();
        
        return baos.toByteArray();
    }
}