package com.example.data_analytics_dashboard.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.example.data_analytics_dashboard.dto.AmountDateDTO;
import com.example.data_analytics_dashboard.dto.DateCountDTO;
import com.example.data_analytics_dashboard.dto.FilterRequest;
import com.example.data_analytics_dashboard.dto.KpiResponse;
import com.example.data_analytics_dashboard.dto.StatusCountDTO;
import com.example.data_analytics_dashboard.entity.Transaction;
import com.example.data_analytics_dashboard.repository.TransactionRepository;

@Service
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    public Page<Transaction> getTransactions(int page, int size) {
        return transactionRepository.findAll(PageRequest.of(page, size));
    }

    public KpiResponse getKpiData() {
        KpiResponse kpi = new KpiResponse();
        kpi.setTotalTransactions(transactionRepository.getTotalTransactions());
        kpi.setTotalAmount(transactionRepository.getTotalAmount());
        kpi.setSuccessCount(transactionRepository.getSuccessCount());
        kpi.setFailedCount(transactionRepository.getFailedCount());
        return kpi;
    }

    public List<StatusCountDTO> getStatusChart() {
        return transactionRepository.getStatusCounts();
    }

    public List<DateCountDTO> getDateChart() {
        return transactionRepository.getDateCounts();
    }

    public List<AmountDateDTO> getAmountChart() {
        return transactionRepository.getAmountByDate();
    }


    public Page<Transaction> filterTransactionsNative(String status, String customerId, 
                                                       String transactionId, String currency,
                                                       int page, int size) {
        int offset = page * size;
        
        List<Transaction> content = transactionRepository.filterTransactionsNative(
            status, customerId, transactionId, currency, offset, size
        );
        
        long total = transactionRepository.countFilteredTransactions(
            status, customerId, transactionId, currency
        );
        
        return new PageImpl<>(content, PageRequest.of(page, size), total);
    }


    public Map<String, Object> getTransactionAggregates(String status, String customerId, 
                                                         String transactionId, String currency) {
        
        Map<String, Object> aggregates = new HashMap<>();
        
        
        long total = transactionRepository.countFilteredTransactions(status, customerId, transactionId, currency);

        long success = transactionRepository.countByStatusAndFilters("success", status, customerId, transactionId, currency);
        long failed = transactionRepository.countByStatusAndFilters("failed", status, customerId, transactionId, currency);
        long pending = total - success - failed;
        
        
        Double totalAmount = transactionRepository.sumAmountByFilters(status, customerId, transactionId, currency);
       
        Double totalDebit = transactionRepository.sumDebitAmount(status, customerId, transactionId, currency);
        Double totalCredit = transactionRepository.sumCreditAmount(status, customerId, transactionId, currency);
        
        
        double debitAmount = totalDebit != null ? totalDebit : 0;
        double creditAmount = totalCredit != null ? totalCredit : 0;
        
        aggregates.put("totalTransactions", total);
        aggregates.put("successCount", success);
        aggregates.put("failedCount", failed);
        aggregates.put("pendingCount", pending);
        aggregates.put("totalAmount", totalAmount != null ? totalAmount : 0);
        aggregates.put("totalDebit", debitAmount);
        aggregates.put("totalCredit", creditAmount);
        aggregates.put("netAmount", creditAmount - debitAmount);
        aggregates.put("avgAmount", total > 0 ? (totalAmount != null ? totalAmount / total : 0) : 0);
        
        return aggregates;
    }

    public List<Transaction> filterAllDynamic(String status, Double amount,
                                             String customerId, String transactionId,
                                             String currency, String baIdentifier) {
        Page<Transaction> page = filterTransactionsNative(status, customerId, transactionId, currency, 0, 500);
        return page.getContent();
    }

    public List<Transaction> filterAll(FilterRequest request) {
        Page<Transaction> page = filterTransactionsNative(
            request.getStatus(),
            request.getCustomerId(),
            request.getTransactionId(),
            request.getCurrency(),
            0, 500
        );
        return page.getContent();
    }
}