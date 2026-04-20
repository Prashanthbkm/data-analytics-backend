package com.example.data_analytics_dashboard.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import jakarta.annotation.PostConstruct;
import java.util.*;

@RestController
@RequestMapping("/api/database")
@CrossOrigin(origins = "*")
public class DatabaseController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @PostConstruct
    public void init() {
        jdbcTemplate.setFetchSize(10000);
    }

    @GetMapping("/all-tables-paginated")
    public ResponseEntity<Map<String, Object>> getAllTablesPaginated(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "500") int size) {

        try {
            String countSql = "SELECT COUNT(*) FROM pg_catalog.pg_tables WHERE schemaname = 'public'";
            int totalTables = jdbcTemplate.queryForObject(countSql, Integer.class);

            String dataSql = "SELECT tablename FROM pg_catalog.pg_tables WHERE schemaname = 'public' ORDER BY tablename OFFSET ? LIMIT ?";
            List<String> tables = jdbcTemplate.queryForList(dataSql, new Object[]{page * size, size}, String.class);

            Map<String, Object> response = new HashMap<>();
            response.put("content", tables);
            response.put("totalElements", totalTables);
            response.put("pageNumber", page);
            response.put("pageSize", size);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            e.printStackTrace();
            Map<String, Object> error = new HashMap<>();
            error.put("error", "Failed to fetch tables: " + e.getMessage());
            error.put("content", new ArrayList<>());
            error.put("totalElements", 0);
            return ResponseEntity.ok(error);
        }
    }

    @GetMapping("/table-data")
    public ResponseEntity<Map<String, Object>> getTableData(
            @RequestParam String tableName,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "100") int size,
            @RequestParam(required = false) String sortBy,
            @RequestParam(defaultValue = "asc") String sortDirection) {

        try {
            String cleanTableName = tableName.replace("\"", "").trim();

            String checkSql = "SELECT COUNT(*) FROM pg_catalog.pg_tables WHERE tablename = ? AND schemaname = 'public'";
            int tableExists = jdbcTemplate.queryForObject(checkSql, Integer.class, cleanTableName);

            if (tableExists == 0) {
                Map<String, Object> error = new HashMap<>();
                error.put("error", "Table does not exist: " + cleanTableName);
                error.put("content", new ArrayList<>());
                error.put("totalElements", 0);
                return ResponseEntity.ok(error);
            }

            String quotedTableName = "\"" + cleanTableName + "\"";

            String countSql = "SELECT COUNT(*) FROM " + quotedTableName;
            long totalRows = jdbcTemplate.queryForObject(countSql, Long.class);

            StringBuilder dataSql = new StringBuilder("SELECT * FROM " + quotedTableName);

            if (sortBy != null && !sortBy.isEmpty() && isValidColumnName(sortBy)) {
                dataSql.append(" ORDER BY \"").append(sortBy).append("\" ").append(sortDirection);
            }

            dataSql.append(" OFFSET ? LIMIT ?");

            List<Map<String, Object>> tableData = jdbcTemplate.queryForList(
                    dataSql.toString(),
                    new Object[]{page * size, size}
            );

            Map<String, Object> response = new HashMap<>();
            response.put("content", tableData);
            response.put("totalElements", totalRows);
            response.put("pageNumber", page);
            response.put("pageSize", size);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            e.printStackTrace();
            Map<String, Object> error = new HashMap<>();
            error.put("error", "Failed to load table: " + e.getMessage());
            error.put("content", new ArrayList<>());
            error.put("totalElements", 0);
            return ResponseEntity.ok(error);
        }
    }

    @GetMapping("/table-columns")
    public ResponseEntity<List<String>> getTableColumns(@RequestParam String tableName) {
        try {
            String cleanTableName = tableName.replace("\"", "").trim();

            String sql = "SELECT column_name FROM information_schema.columns WHERE table_name = ? AND table_schema = 'public' ORDER BY ordinal_position";

            List<String> columns = jdbcTemplate.query(
                    sql,
                    new Object[]{cleanTableName},
                    (rs, rowNum) -> rs.getString("column_name")
            );

            return ResponseEntity.ok(columns);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.ok(new ArrayList<>());
        }
    }

    private boolean isValidColumnName(String columnName) {
        return columnName != null && columnName.matches("^[a-zA-Z0-9_]+$");
    }
}