package com.example.data_analytics_dashboard.dto;

import java.time.LocalDate;

public class AmountDateDTO {

    private LocalDate date;
    private Double totalAmount;

    public AmountDateDTO() {}

    public AmountDateDTO(LocalDate date, Double totalAmount) {
        this.date = date;
        this.totalAmount = totalAmount;
    }

    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }

    public Double getTotalAmount() { return totalAmount; }
    public void setTotalAmount(Double totalAmount) { this.totalAmount = totalAmount; }
}