package com.example.data_analytics_dashboard.dto;

import java.math.BigDecimal;

public class AggregateResultDTO {

    private long totalTransactions;
    private long successCount;
    private long failedCount;
    private long pendingCount;
    private BigDecimal totalDebitAmount;
    private BigDecimal totalCreditAmount;
    private BigDecimal netAmount;
    private double avgDebitAmount;
    private double avgCreditAmount;
    private BigDecimal totalAmount;

    public AggregateResultDTO() {}

    public AggregateResultDTO(long totalTransactions, long successCount, long failedCount, long pendingCount,
                              BigDecimal totalDebitAmount, BigDecimal totalCreditAmount, BigDecimal netAmount,
                              double avgDebitAmount, double avgCreditAmount, BigDecimal totalAmount) {

        this.totalTransactions = totalTransactions;
        this.successCount = successCount;
        this.failedCount = failedCount;
        this.pendingCount = pendingCount;
        this.totalDebitAmount = totalDebitAmount;
        this.totalCreditAmount = totalCreditAmount;
        this.netAmount = netAmount;
        this.avgDebitAmount = avgDebitAmount;
        this.avgCreditAmount = avgCreditAmount;
        this.totalAmount = totalAmount;
    }

    // Getters & Setters
    public long getTotalTransactions() { return totalTransactions; }
    public void setTotalTransactions(long totalTransactions) { this.totalTransactions = totalTransactions; }

    public long getSuccessCount() { return successCount; }
    public void setSuccessCount(long successCount) { this.successCount = successCount; }

    public long getFailedCount() { return failedCount; }
    public void setFailedCount(long failedCount) { this.failedCount = failedCount; }

    public long getPendingCount() { return pendingCount; }
    public void setPendingCount(long pendingCount) { this.pendingCount = pendingCount; }

    public BigDecimal getTotalDebitAmount() { return totalDebitAmount; }
    public void setTotalDebitAmount(BigDecimal totalDebitAmount) { this.totalDebitAmount = totalDebitAmount; }

    public BigDecimal getTotalCreditAmount() { return totalCreditAmount; }
    public void setTotalCreditAmount(BigDecimal totalCreditAmount) { this.totalCreditAmount = totalCreditAmount; }

    public BigDecimal getNetAmount() { return netAmount; }
    public void setNetAmount(BigDecimal netAmount) { this.netAmount = netAmount; }

    public double getAvgDebitAmount() { return avgDebitAmount; }
    public void setAvgDebitAmount(double avgDebitAmount) { this.avgDebitAmount = avgDebitAmount; }

    public double getAvgCreditAmount() { return avgCreditAmount; }
    public void setAvgCreditAmount(double avgCreditAmount) { this.avgCreditAmount = avgCreditAmount; }

    public BigDecimal getTotalAmount() { return totalAmount; }
    public void setTotalAmount(BigDecimal totalAmount) { this.totalAmount = totalAmount; }
}