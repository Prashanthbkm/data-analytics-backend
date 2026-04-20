package com.example.data_analytics_dashboard.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.data_analytics_dashboard.dto.AmountDateDTO;
import com.example.data_analytics_dashboard.dto.DateCountDTO;
import com.example.data_analytics_dashboard.dto.FilterRequest;
import com.example.data_analytics_dashboard.dto.KpiResponse;
import com.example.data_analytics_dashboard.dto.StatusCountDTO;
import com.example.data_analytics_dashboard.entity.Transaction;
import com.example.data_analytics_dashboard.service.TransactionService;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @GetMapping("/transactions")
    public Page<Transaction> getTransactions(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size
    ) {
        System.out.println("🔥 GET TRANSACTIONS HIT");
        return transactionService.getTransactions(page, size);
    }

    @GetMapping("/kpi")
    public KpiResponse getKpi() {
        System.out.println("🔥 KPI API HIT");
        return transactionService.getKpiData();
    }

    @GetMapping("/charts/status")
    public List<StatusCountDTO> getStatusChart() {
        System.out.println("🔥 STATUS CHART HIT");
        return transactionService.getStatusChart();
    }

    @GetMapping("/charts/date")
    public List<DateCountDTO> getDateChart() {
        System.out.println("DATE CHART HIT");
        return transactionService.getDateChart();
    }

    @GetMapping("/charts/amount")
    public List<AmountDateDTO> getAmountChart() {
        System.out.println("AMOUNT CHART HIT");
        return transactionService.getAmountChart();
    }

    // 🔥 FILTER API with pagination
    @PostMapping("/transactions/filter")
    public Page<Transaction> filterAll(
            @RequestBody FilterRequest request,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "500") int size
    ) {
        System.out.println("FILTER API HIT");
        System.out.println("Page: " + page + ", Size: " + size);

        try {
            Page<Transaction> result;

            if (request.getFilters() != null && !request.getFilters().isEmpty()) {
                Map<String, String> filters = request.getFilters();
                String status = filters.get("status");
                String customerId = filters.get("customerId");
                String transactionId = filters.get("transactionId");
                String currency = filters.get("currency");

                result = transactionService.filterTransactionsNative(
                    status, customerId, transactionId, currency, page, size
                );
            } else {
                result = transactionService.filterTransactionsNative(
                    request.getStatus(),
                    request.getCustomerId(),
                    request.getTransactionId(),
                    request.getCurrency(),
                    page, size
                );
            }

            System.out.println("FILTER RESULT SIZE: " + result.getContent().size());
            System.out.println("TOTAL ELEMENTS: " + result.getTotalElements());
            return result;

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("ERROR IN FILTER API: " + e.getMessage());
            return transactionService.getTransactions(page, size);
        }
    }
    
   
    @PostMapping("/transactions/aggregate")
    public ResponseEntity<Map<String, Object>> getTransactionAggregates(@RequestBody FilterRequest request) {
        System.out.println("AGGREGATE API HIT");
        
        try {
            Map<String, String> filters = request.getFilters();
            String status = filters != null ? filters.get("status") : null;
            String customerId = filters != null ? filters.get("customerId") : null;
            String transactionId = filters != null ? filters.get("transactionId") : null;
            String currency = filters != null ? filters.get("currency") : null;
            
            // If no filters, use individual fields
            if (status == null && customerId == null && transactionId == null && currency == null) {
                status = request.getStatus();
                customerId = request.getCustomerId();
                transactionId = request.getTransactionId();
                currency = request.getCurrency();
            }
            
            Map<String, Object> aggregates = transactionService.getTransactionAggregates(
                status, customerId, transactionId, currency
            );
            
            return ResponseEntity.ok(aggregates);
            
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }
}