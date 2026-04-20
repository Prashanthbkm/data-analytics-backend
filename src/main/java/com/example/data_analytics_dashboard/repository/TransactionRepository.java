package com.example.data_analytics_dashboard.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.data_analytics_dashboard.dto.AmountDateDTO;
import com.example.data_analytics_dashboard.dto.DateCountDTO;
import com.example.data_analytics_dashboard.dto.StatusCountDTO;
import com.example.data_analytics_dashboard.entity.Transaction;

public interface TransactionRepository extends JpaRepository<Transaction, String> {

    @Override
    Page<Transaction> findAll(Pageable pageable);

    @Query("SELECT COUNT(t) FROM Transaction t")
    long getTotalTransactions();

    @Query("SELECT COALESCE(SUM(t.amount), 0) FROM Transaction t")
    double getTotalAmount();

    @Query("SELECT COUNT(t) FROM Transaction t WHERE LOWER(t.status) = 'success'")
    long getSuccessCount();

    @Query("SELECT COUNT(t) FROM Transaction t WHERE LOWER(t.status) = 'failed'")
    long getFailedCount();

    @Query("SELECT new com.example.data_analytics_dashboard.dto.StatusCountDTO(t.status, COUNT(t)) " +
           "FROM Transaction t GROUP BY t.status")
    List<StatusCountDTO> getStatusCounts();

    @Query("SELECT new com.example.data_analytics_dashboard.dto.DateCountDTO(t.transactionDate, COUNT(t)) " +
           "FROM Transaction t GROUP BY t.transactionDate ORDER BY t.transactionDate")
    List<DateCountDTO> getDateCounts();

    @Query("SELECT new com.example.data_analytics_dashboard.dto.AmountDateDTO(t.transactionDate, COALESCE(SUM(t.amount), 0)) " +
           "FROM Transaction t GROUP BY t.transactionDate ORDER BY t.transactionDate")
    List<AmountDateDTO> getAmountByDate();

    
    @Query(value = "SELECT * FROM \"recoengine_load_testing_dummy_data_6\" WHERE " +
           "(:status IS NULL OR \"Status\" ILIKE CONCAT('%', CAST(:status AS text), '%')) AND " +
           "(:customerId IS NULL OR \"CustomerID\" ILIKE CONCAT('%', CAST(:customerId AS text), '%')) AND " +
           "(:transactionId IS NULL OR \"TransactionID\" ILIKE CONCAT('%', CAST(:transactionId AS text), '%')) AND " +
           "(:currency IS NULL OR \"Currency\" ILIKE CONCAT('%', CAST(:currency AS text), '%')) " +
           "OFFSET :offset LIMIT :limit", 
           nativeQuery = true)
    List<Transaction> filterTransactionsNative(
        @Param("status") String status,
        @Param("customerId") String customerId,
        @Param("transactionId") String transactionId,
        @Param("currency") String currency,
        @Param("offset") int offset,
        @Param("limit") int limit
    );
    
    
    @Query(value = "SELECT COUNT(*) FROM \"recoengine_load_testing_dummy_data_6\" WHERE " +
           "(:status IS NULL OR \"Status\" ILIKE CONCAT('%', CAST(:status AS text), '%')) AND " +
           "(:customerId IS NULL OR \"CustomerID\" ILIKE CONCAT('%', CAST(:customerId AS text), '%')) AND " +
           "(:transactionId IS NULL OR \"TransactionID\" ILIKE CONCAT('%', CAST(:transactionId AS text), '%')) AND " +
           "(:currency IS NULL OR \"Currency\" ILIKE CONCAT('%', CAST(:currency AS text), '%'))", 
           nativeQuery = true)
    long countFilteredTransactions(
        @Param("status") String status,
        @Param("customerId") String customerId,
        @Param("transactionId") String transactionId,
        @Param("currency") String currency
    );
    
    
    @Query(value = "SELECT COUNT(*) FROM \"recoengine_load_testing_dummy_data_6\" WHERE " +
           "(:status IS NULL OR \"Status\" ILIKE CONCAT('%', CAST(:status AS text), '%')) AND " +
           "(:customerId IS NULL OR \"CustomerID\" ILIKE CONCAT('%', CAST(:customerId AS text), '%')) AND " +
           "(:transactionId IS NULL OR \"TransactionID\" ILIKE CONCAT('%', CAST(:transactionId AS text), '%')) AND " +
           "(:currency IS NULL OR \"Currency\" ILIKE CONCAT('%', CAST(:currency AS text), '%')) AND " +
           "LOWER(\"Status\") = LOWER(:filterStatus)", 
           nativeQuery = true)
    long countByStatusAndFilters(
        @Param("filterStatus") String filterStatus,
        @Param("status") String status,
        @Param("customerId") String customerId,
        @Param("transactionId") String transactionId,
        @Param("currency") String currency
    );
    
   
    @Query(value = "SELECT COALESCE(SUM(\"Amount\"), 0) FROM \"recoengine_load_testing_dummy_data_6\" WHERE " +
           "(:status IS NULL OR \"Status\" ILIKE CONCAT('%', CAST(:status AS text), '%')) AND " +
           "(:customerId IS NULL OR \"CustomerID\" ILIKE CONCAT('%', CAST(:customerId AS text), '%')) AND " +
           "(:transactionId IS NULL OR \"TransactionID\" ILIKE CONCAT('%', CAST(:transactionId AS text), '%')) AND " +
           "(:currency IS NULL OR \"Currency\" ILIKE CONCAT('%', CAST(:currency AS text), '%'))", 
           nativeQuery = true)
    Double sumAmountByFilters(
        @Param("status") String status,
        @Param("customerId") String customerId,
        @Param("transactionId") String transactionId,
        @Param("currency") String currency
    );
  
    @Query(value = "SELECT COALESCE(SUM(ABS(\"Amount\")), 0) FROM \"recoengine_load_testing_dummy_data_6\" WHERE " +
           "(:status IS NULL OR \"Status\" ILIKE CONCAT('%', CAST(:status AS text), '%')) AND " +
           "(:customerId IS NULL OR \"CustomerID\" ILIKE CONCAT('%', CAST(:customerId AS text), '%')) AND " +
           "(:transactionId IS NULL OR \"TransactionID\" ILIKE CONCAT('%', CAST(:transactionId AS text), '%')) AND " +
           "(:currency IS NULL OR \"Currency\" ILIKE CONCAT('%', CAST(:currency AS text), '%')) AND " +
           "\"Amount\" < 0", 
           nativeQuery = true)
    Double sumDebitAmount(
        @Param("status") String status,
        @Param("customerId") String customerId,
        @Param("transactionId") String transactionId,
        @Param("currency") String currency
    );
    

    @Query(value = "SELECT COALESCE(SUM(\"Amount\"), 0) FROM \"recoengine_load_testing_dummy_data_6\" WHERE " +
           "(:status IS NULL OR \"Status\" ILIKE CONCAT('%', CAST(:status AS text), '%')) AND " +
           "(:customerId IS NULL OR \"CustomerID\" ILIKE CONCAT('%', CAST(:customerId AS text), '%')) AND " +
           "(:transactionId IS NULL OR \"TransactionID\" ILIKE CONCAT('%', CAST(:transactionId AS text), '%')) AND " +
           "(:currency IS NULL OR \"Currency\" ILIKE CONCAT('%', CAST(:currency AS text), '%')) AND " +
           "\"Amount\" > 0", 
           nativeQuery = true)
    Double sumCreditAmount(
        @Param("status") String status,
        @Param("customerId") String customerId,
        @Param("transactionId") String transactionId,
        @Param("currency") String currency
    );
}