package com.example.data_analytics_dashboard.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
public class AIService {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${gemini.api.key:}")
    private String apiKey;

    private String workingModelUrl = null;

    private final String[] MODEL_CANDIDATES = {
            "gemini-2.0-flash-latest",
            "gemini-2.0-flash",
            "gemini-1.5-pro-latest",
            "gemini-1.5-flash-latest",
            "gemini-1.5-pro",
            "gemini-pro"
    };

    private final String API_BASE = "https://generativelanguage.googleapis.com/v1beta/models/";
    private final String API_SUFFIX = ":generateContent";

    public String askGemini(String question, Map<String, Object> kpiData) {

        if (apiKey == null || apiKey.isEmpty()) {
            return generateFallbackResponse(question, kpiData);
        }

        try {
            if (workingModelUrl == null) {
                discoverWorkingModel();
            }

            if (workingModelUrl == null) {
                return generateFallbackResponse(question, kpiData);
            }

            if (kpiData == null) {
                kpiData = new HashMap<>();
            }

            String prompt = buildPrompt(question, kpiData);

            Map<String, Object> requestBody = new HashMap<>();
            Map<String, Object> content = new HashMap<>();
            Map<String, Object> part = new HashMap<>();

            part.put("text", prompt);
            content.put("parts", List.of(part));
            requestBody.put("contents", List.of(content));

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            String url = workingModelUrl + "?key=" + apiKey;

            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

            ResponseEntity<Map> response = restTemplate.postForEntity(url, entity, Map.class);

            Map<String, Object> responseBody = response.getBody();
            List<Map<String, Object>> candidates = (List<Map<String, Object>>) responseBody.get("candidates");

            if (candidates != null && !candidates.isEmpty()) {
                Map<String, Object> first = candidates.get(0);
                Map<String, Object> contentRes = (Map<String, Object>) first.get("content");
                List<Map<String, Object>> parts = (List<Map<String, Object>>) contentRes.get("parts");

                return (String) parts.get(0).get("text");
            }

            return generateFallbackResponse(question, kpiData);

        } catch (Exception e) {
            e.printStackTrace();
            workingModelUrl = null;
            return generateFallbackResponse(question, kpiData);
        }
    }

    private void discoverWorkingModel() {

        for (String model : MODEL_CANDIDATES) {
            try {
                String testUrl = API_BASE + model + API_SUFFIX + "?key=" + apiKey;

                Map<String, Object> body = new HashMap<>();
                body.put("contents", List.of(Map.of("parts", List.of(Map.of("text", "test")))));

                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);

                restTemplate.postForEntity(testUrl, new HttpEntity<>(body, headers), Map.class);

                workingModelUrl = API_BASE + model + API_SUFFIX;
                return;

            } catch (Exception ignored) {}
        }
    }

    private String buildPrompt(String question, Map<String, Object> data) {

        long total = getLong(data, "totalTransactions");
        long success = getLong(data, "successCount");
        long failed = getLong(data, "failedCount");

        double amount = getDouble(data, "totalAmount");

        return String.format(
                "Total: %d, Success: %d, Failed: %d, Amount: %.2f. Answer: %s",
                total, success, failed, amount, question
        );
    }

    private String generateFallbackResponse(String q, Map<String, Object> data) {
        return "AI service not available. Showing basic insights.";
    }

    private long getLong(Map<String, Object> m, String k) {
        Object v = m.get(k);
        return (v instanceof Number) ? ((Number) v).longValue() : 0;
    }

    private double getDouble(Map<String, Object> m, String k) {
        Object v = m.get(k);
        return (v instanceof Number) ? ((Number) v).doubleValue() : 0;
    }
}