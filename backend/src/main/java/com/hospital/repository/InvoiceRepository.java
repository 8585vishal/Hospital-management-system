package com.hospital.repository;

import com.hospital.entity.Invoice;
import com.hospital.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, Long> {
    
    Optional<Invoice> findByInvoiceNumber(String invoiceNumber);
    
    List<Invoice> findByPatient(Patient patient);
    
    List<Invoice> findByStatus(Invoice.InvoiceStatus status);
    
    List<Invoice> findByPatientOrderByCreatedAtDesc(Patient patient);
    
    @Query("SELECT i FROM Invoice i WHERE i.createdAt BETWEEN :start AND :end ORDER BY i.createdAt DESC")
    List<Invoice> findByDateRange(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);
    
    @Query("SELECT COUNT(i) FROM Invoice i WHERE i.status = :status")
    Long countByStatus(@Param("status") Invoice.InvoiceStatus status);
    
    @Query("SELECT SUM(i.totalAmount) FROM Invoice i WHERE i.status = 'PAID' AND i.paymentDate BETWEEN :start AND :end")
    Double getTotalRevenue(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);
    
    @Query("SELECT SUM(i.totalAmount) FROM Invoice i WHERE i.status = 'PENDING'")
    Double getTotalOutstanding();
    
    @Query("SELECT i FROM Invoice i WHERE i.dueDate < :currentDate AND i.status = 'PENDING' ORDER BY i.dueDate")
    List<Invoice> findOverdueInvoices(@Param("currentDate") LocalDateTime currentDate);
}