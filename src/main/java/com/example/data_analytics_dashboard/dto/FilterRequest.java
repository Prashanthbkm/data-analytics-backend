package com.example.data_analytics_dashboard.dto;

import java.time.LocalDate;
import java.util.Map;

public class FilterRequest {

    private String transactionId;
    private String customerId;
    private String status;
    private String currency;

    private Map<String, String> filters;
    private Map<String, Boolean> columns;

    public String getTransactionId() { return transactionId; }
    public void setTransactionId(String transactionId) { this.transactionId = transactionId; }

    public String getCustomerId() { return customerId; }
    public void setCustomerId(String customerId) { this.customerId = customerId; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }

    public Map<String, String> getFilters() { return filters; }
    public void setFilters(Map<String, String> filters) { this.filters = filters; }

    public Map<String, Boolean> getColumns() { return columns; }
    public void setColumns(Map<String, Boolean> columns) { this.columns = columns; }
}