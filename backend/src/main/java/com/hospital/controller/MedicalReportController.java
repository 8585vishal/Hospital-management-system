package com.hospital.controller;

import com.hospital.entity.MedicalReport;
import com.hospital.service.MedicalReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/medical-reports")
@CrossOrigin(origins = "*")
public class MedicalReportController {
    
    @Autowired
    private MedicalReportService medicalReportService;
    
    @GetMapping
    public ResponseEntity<List<MedicalReport>> getAllReports() {
        List<MedicalReport> reports = medicalReportService.getAllReports();
        return ResponseEntity.ok(reports);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<MedicalReport> getReportById(@PathVariable Long id) {
        Optional<MedicalReport> report = medicalReportService.getReportById(id);
        return report.map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
    }
    
    @PostMapping
    public ResponseEntity<MedicalReport> createReport(@Valid @RequestBody MedicalReport report) {
        try {
            MedicalReport savedReport = medicalReportService.saveReport(report);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedReport);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @PostMapping("/create")
    public ResponseEntity<MedicalReport> createReport(
            @RequestParam Long patientId,
            @RequestParam Long doctorId,
            @RequestParam String reportType,
            @RequestParam String title,
            @RequestParam String description) {
        try {
            MedicalReport report = medicalReportService.createReport(patientId, doctorId, reportType, title, description);
            return ResponseEntity.status(HttpStatus.CREATED).body(report);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<MedicalReport> updateReport(@PathVariable Long id, @Valid @RequestBody MedicalReport reportDetails) {
        try {
            MedicalReport updatedReport = medicalReportService.updateReport(id, reportDetails);
            return ResponseEntity.ok(updatedReport);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReport(@PathVariable Long id) {
        try {
            medicalReportService.deleteReport(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<MedicalReport>> getReportsByPatient(@PathVariable Long patientId) {
        try {
            List<MedicalReport> reports = medicalReportService.getReportsByPatient(patientId);
            return ResponseEntity.ok(reports);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @GetMapping("/doctor/{doctorId}")
    public ResponseEntity<List<MedicalReport>> getReportsByDoctor(@PathVariable Long doctorId) {
        try {
            List<MedicalReport> reports = medicalReportService.getReportsByDoctor(doctorId);
            return ResponseEntity.ok(reports);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @GetMapping("/type/{reportType}")
    public ResponseEntity<List<MedicalReport>> getReportsByType(@PathVariable String reportType) {
        List<MedicalReport> reports = medicalReportService.getReportsByType(reportType);
        return ResponseEntity.ok(reports);
    }
    
    @GetMapping("/search")
    public ResponseEntity<List<MedicalReport>> searchReports(@RequestParam String query) {
        List<MedicalReport> reports = medicalReportService.searchReports(query);
        return ResponseEntity.ok(reports);
    }
    
    @GetMapping("/types")
    public ResponseEntity<List<String>> getAllReportTypes() {
        List<String> types = medicalReportService.getAllReportTypes();
        return ResponseEntity.ok(types);
    }
    
    @PostMapping("/generate-random")
    public ResponseEntity<MedicalReport> generateRandomReport() {
        try {
            MedicalReport report = medicalReportService.generateRandomReport();
            return ResponseEntity.status(HttpStatus.CREATED).body(report);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @GetMapping("/{id}/download")
    public ResponseEntity<byte[]> downloadReport(@PathVariable Long id) {
        try {
            byte[] pdfBytes = medicalReportService.generateReportPDF(id);
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", "medical-report-" + id + ".pdf");
            headers.setContentLength(pdfBytes.length);
            
            return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}