package com.hospital.service;

import com.hospital.entity.Invoice;
import com.hospital.entity.InvoiceItem;
import com.hospital.entity.Patient;
import com.hospital.repository.InvoiceRepository;
import com.hospital.repository.PatientRepository;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
public class InvoiceService {
    
    @Autowired
    private InvoiceRepository invoiceRepository;
    
    @Autowired
    private PatientRepository patientRepository;
    
    private final Random random = new Random();
    
    public List<Invoice> getAllInvoices() {
        return invoiceRepository.findAll();
    }
    
    public Optional<Invoice> getInvoiceById(Long id) {
        return invoiceRepository.findById(id);
    }
    
    public Optional<Invoice> getInvoiceByNumber(String invoiceNumber) {
        return invoiceRepository.findByInvoiceNumber(invoiceNumber);
    }
    
    public Invoice saveInvoice(Invoice invoice) {
        return invoiceRepository.save(invoice);
    }
    
    public Invoice createInvoice(Long patientId, Double subtotal, Double taxAmount, Double discountAmount) {
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new RuntimeException("Patient not found with id: " + patientId));
        
        Double totalAmount = subtotal + (taxAmount != null ? taxAmount : 0) - (discountAmount != null ? discountAmount : 0);
        
        Invoice invoice = new Invoice(patient, subtotal, totalAmount);
        invoice.setTaxAmount(taxAmount);
        invoice.setDiscountAmount(discountAmount);
        
        return invoiceRepository.save(invoice);
    }
    
    public Invoice updateInvoice(Long id, Invoice invoiceDetails) {
        Invoice invoice = invoiceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Invoice not found with id: " + id));
        
        if (invoiceDetails.getSubtotal() != null) {
            invoice.setSubtotal(invoiceDetails.getSubtotal());
        }
        if (invoiceDetails.getTaxAmount() != null) {
            invoice.setTaxAmount(invoiceDetails.getTaxAmount());
        }
        if (invoiceDetails.getDiscountAmount() != null) {
            invoice.setDiscountAmount(invoiceDetails.getDiscountAmount());
        }
        if (invoiceDetails.getTotalAmount() != null) {
            invoice.setTotalAmount(invoiceDetails.getTotalAmount());
        }
        if (invoiceDetails.getStatus() != null) {
            invoice.setStatus(invoiceDetails.getStatus());
        }
        if (invoiceDetails.getPaymentMethod() != null) {
            invoice.setPaymentMethod(invoiceDetails.getPaymentMethod());
        }
        if (invoiceDetails.getPaymentDate() != null) {
            invoice.setPaymentDate(invoiceDetails.getPaymentDate());
        }
        if (invoiceDetails.getNotes() != null) {
            invoice.setNotes(invoiceDetails.getNotes());
        }
        
        return invoiceRepository.save(invoice);
    }
    
    public void deleteInvoice(Long id) {
        Invoice invoice = invoiceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Invoice not found with id: " + id));
        invoiceRepository.delete(invoice);
    }
    
    public List<Invoice> getInvoicesByPatient(Long patientId) {
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new RuntimeException("Patient not found with id: " + patientId));
        return invoiceRepository.findByPatientOrderByCreatedAtDesc(patient);
    }
    
    public List<Invoice> getInvoicesByStatus(Invoice.InvoiceStatus status) {
        return invoiceRepository.findByStatus(status);
    }
    
    public List<Invoice> getInvoicesByDateRange(LocalDateTime start, LocalDateTime end) {
        return invoiceRepository.findByDateRange(start, end);
    }
    
    public Double getTotalRevenue(LocalDateTime start, LocalDateTime end) {
        Double revenue = invoiceRepository.getTotalRevenue(start, end);
        return revenue != null ? revenue : 0.0;
    }
    
    public Double getTotalOutstanding() {
        Double outstanding = invoiceRepository.getTotalOutstanding();
        return outstanding != null ? outstanding : 0.0;
    }
    
    public List<Invoice> getOverdueInvoices() {
        return invoiceRepository.findOverdueInvoices(LocalDateTime.now());
    }
    
    public Long getInvoiceCountByStatus(Invoice.InvoiceStatus status) {
        return invoiceRepository.countByStatus(status);
    }
    
    public Invoice markAsPaid(Long id, String paymentMethod) {
        Invoice invoice = invoiceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Invoice not found with id: " + id));
        
        invoice.setStatus(Invoice.InvoiceStatus.PAID);
        invoice.setPaymentMethod(paymentMethod);
        invoice.setPaymentDate(LocalDateTime.now());
        
        return invoiceRepository.save(invoice);
    }
    
    // Generate random invoice for demonstration
    public Invoice generateRandomInvoice() {
        List<Patient> patients = patientRepository.findAll();
        
        if (patients.isEmpty()) {
            throw new RuntimeException("Need at least one patient to generate invoice");
        }
        
        Patient randomPatient = patients.get(random.nextInt(patients.size()));
        
        String[] services = {"Consultation", "Blood Test", "X-Ray", "MRI Scan", "Surgery", "Emergency Treatment", "Physical Therapy"};
        Double[] prices = {150.0, 75.0, 200.0, 800.0, 2500.0, 500.0, 120.0};
        
        List<InvoiceItem> items = new ArrayList<>();
        Double subtotal = 0.0;
        
        // Generate 1-4 random items
        int numItems = random.nextInt(4) + 1;
        for (int i = 0; i < numItems; i++) {
            int serviceIndex = random.nextInt(services.length);
            String service = services[serviceIndex];
            Double price = prices[serviceIndex];
            int quantity = random.nextInt(3) + 1;
            
            InvoiceItem item = new InvoiceItem();
            item.setDescription(service);
            item.setQuantity(quantity);
            item.setUnitPrice(price);
            item.setTotalPrice(quantity * price);
            item.setItemType("Service");
            
            items.add(item);
            subtotal += item.getTotalPrice();
        }
        
        Double taxAmount = subtotal * 0.1; // 10% tax
        Double discountAmount = random.nextBoolean() ? subtotal * 0.05 : 0.0; // 5% discount sometimes
        Double totalAmount = subtotal + taxAmount - discountAmount;
        
        Invoice invoice = new Invoice(randomPatient, subtotal, totalAmount);
        invoice.setTaxAmount(taxAmount);
        invoice.setDiscountAmount(discountAmount);
        invoice.setStatus(random.nextBoolean() ? Invoice.InvoiceStatus.PAID : Invoice.InvoiceStatus.PENDING);
        
        if (invoice.getStatus() == Invoice.InvoiceStatus.PAID) {
            invoice.setPaymentMethod("Credit Card");
            invoice.setPaymentDate(LocalDateTime.now().minusDays(random.nextInt(30)));
        }
        
        Invoice savedInvoice = invoiceRepository.save(invoice);
        
        // Set invoice reference for items and save them
        for (InvoiceItem item : items) {
            item.setInvoice(savedInvoice);
        }
        
        savedInvoice.setInvoiceItems(items);
        return invoiceRepository.save(savedInvoice);
    }
    
    public byte[] generateInvoicePDF(Long invoiceId) throws DocumentException, IOException {
        Invoice invoice = invoiceRepository.findById(invoiceId)
                .orElseThrow(() -> new RuntimeException("Invoice not found with id: " + invoiceId));
        
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Document document = new Document();
        PdfWriter.getInstance(document, baos);
        
        document.open();
        
        // Title
        Font titleFont = new Font(Font.FontFamily.HELVETICA, 20, Font.BOLD);
        Paragraph title = new Paragraph("INVOICE", titleFont);
        title.setAlignment(Element.ALIGN_CENTER);
        title.setSpacingAfter(20);
        document.add(title);
        
        // Invoice details
        Font headerFont = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD);
        Font normalFont = new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL);
        
        document.add(new Paragraph("Invoice Number: " + invoice.getInvoiceNumber(), headerFont));
        document.add(new Paragraph("Date: " + invoice.getCreatedAt().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")), normalFont));
        document.add(new Paragraph("Status: " + invoice.getStatus(), normalFont));
        document.add(new Paragraph(" "));
        
        // Patient details
        document.add(new Paragraph("BILL TO:", headerFont));
        document.add(new Paragraph(invoice.getPatient().getFullName(), normalFont));
        document.add(new Paragraph(invoice.getPatient().getEmail(), normalFont));
        document.add(new Paragraph(invoice.getPatient().getPhone(), normalFont));
        if (invoice.getPatient().getAddress() != null) {
            document.add(new Paragraph(invoice.getPatient().getAddress(), normalFont));
        }
        document.add(new Paragraph(" "));
        
        // Items table
        if (invoice.getInvoiceItems() != null && !invoice.getInvoiceItems().isEmpty()) {
            PdfPTable table = new PdfPTable(4);
            table.setWidthPercentage(100);
            table.setSpacingBefore(10f);
            
            // Headers
            table.addCell(new Phrase("Description", headerFont));
            table.addCell(new Phrase("Quantity", headerFont));
            table.addCell(new Phrase("Unit Price", headerFont));
            table.addCell(new Phrase("Total", headerFont));
            
            // Items
            for (InvoiceItem item : invoice.getInvoiceItems()) {
                table.addCell(new Phrase(item.getDescription(), normalFont));
                table.addCell(new Phrase(item.getQuantity().toString(), normalFont));
                table.addCell(new Phrase("$" + String.format("%.2f", item.getUnitPrice()), normalFont));
                table.addCell(new Phrase("$" + String.format("%.2f", item.getTotalPrice()), normalFont));
            }
            
            document.add(table);
        }
        
        // Totals
        document.add(new Paragraph(" "));
        document.add(new Paragraph("Subtotal: $" + String.format("%.2f", invoice.getSubtotal()), normalFont));
        if (invoice.getTaxAmount() != null && invoice.getTaxAmount() > 0) {
            document.add(new Paragraph("Tax: $" + String.format("%.2f", invoice.getTaxAmount()), normalFont));
        }
        if (invoice.getDiscountAmount() != null && invoice.getDiscountAmount() > 0) {
            document.add(new Paragraph("Discount: -$" + String.format("%.2f", invoice.getDiscountAmount()), normalFont));
        }
        document.add(new Paragraph("TOTAL: $" + String.format("%.2f", invoice.getTotalAmount()), headerFont));
        
        if (invoice.getStatus() == Invoice.InvoiceStatus.PAID && invoice.getPaymentDate() != null) {
            document.add(new Paragraph(" "));
            document.add(new Paragraph("Payment Date: " + invoice.getPaymentDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")), normalFont));
            document.add(new Paragraph("Payment Method: " + invoice.getPaymentMethod(), normalFont));
        }
        
        document.close();
        
        return baos.toByteArray();
    }
}