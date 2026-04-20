package com.example.data_analytics_dashboard.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "recoengine_load_testing_dummy_data_6")
public class Transaction {

    @Id
    @Column(name = "\"BA_IDENTIFIER\"")
    private String baIdentifier;

    @Column(name = "\"TransactionID\"")
    private String transactionId;

    @Column(name = "\"CustomerID\"")
    private String customerId;

    @Column(name = "\"TransactionDate\"")
    private LocalDate transactionDate;

    @Column(name = "\"Amount\"")
    private Double amount;

    @Column(name = "\"Status\"")
    private String status;

    @Column(name = "\"Currency\"")
    private String currency;

    @Column(name = "\"PaymentMethod\"")
    private String paymentMethod;

    @Column(name = "\"BA_RECO_STATUS\"")
    private String baRecoStatus;

    @Column(name = "\"BA_BATCH_ID\"")
    private String baBatchId;

    @Column(name = "\"BA_UPLOAD_DATE\"")
    private LocalDateTime baUploadDate;

    @Column(name = "\"BA_RECO_DATE\"")
    private LocalDateTime baRecoDate;

    @Column(name = "\"BA_RECO_AMOUNT\"")
    private Double baRecoAmount;

    @Column(name = "\"BA_TOLERANCE\"")
    private Long baTolerance;

    @Column(name = "\"BA_RECO_REMARKS\"")
    private String baRecoRemarks;

    // Getters & Setters

    public String getBaIdentifier() { return baIdentifier; }
    public void setBaIdentifier(String baIdentifier) { this.baIdentifier = baIdentifier; }

    public String getTransactionId() { return transactionId; }
    public void setTransactionId(String transactionId) { this.transactionId = transactionId; }

    public String getCustomerId() { return customerId; }
    public void setCustomerId(String customerId) { this.customerId = customerId; }

    public LocalDate getTransactionDate() { return transactionDate; }
    public void setTransactionDate(LocalDate transactionDate) { this.transactionDate = transactionDate; }

    public Double getAmount() { return amount; }
    public void setAmount(Double amount) { this.amount = amount; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }

    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }

    public String getBaRecoStatus() { return baRecoStatus; }
    public void setBaRecoStatus(String baRecoStatus) { this.baRecoStatus = baRecoStatus; }

    public String getBaBatchId() { return baBatchId; }
    public void setBaBatchId(String baBatchId) { this.baBatchId = baBatchId; }

    public LocalDateTime getBaUploadDate() { return baUploadDate; }
    public void setBaUploadDate(LocalDateTime baUploadDate) { this.baUploadDate = baUploadDate; }

    public LocalDateTime getBaRecoDate() { return baRecoDate; }
    public void setBaRecoDate(LocalDateTime baRecoDate) { this.baRecoDate = baRecoDate; }

    public Double getBaRecoAmount() { return baRecoAmount; }
    public void setBaRecoAmount(Double baRecoAmount) { this.baRecoAmount = baRecoAmount; }

    public Long getBaTolerance() { return baTolerance; }
    public void setBaTolerance(Long baTolerance) { this.baTolerance = baTolerance; }

    public String getBaRecoRemarks() { return baRecoRemarks; }
    public void setBaRecoRemarks(String baRecoRemarks) { this.baRecoRemarks = baRecoRemarks; }
}