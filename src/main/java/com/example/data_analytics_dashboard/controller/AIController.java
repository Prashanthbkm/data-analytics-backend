package com.example.data_analytics_dashboard.controller;

import com.example.data_analytics_dashboard.service.AIService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/ai")
@CrossOrigin(origins = "*")
public class AIController {

    @Autowired
    private AIService aiService;

    @PostMapping("/ask")
    public Map<String, String> askQuestion(@RequestBody Map<String, Object> request) {

        String question = (String) request.get("question");
        Map<String, Object> kpiData = (Map<String, Object>) request.get("kpiData");

        if (kpiData == null) {
            kpiData = new HashMap<>();
        }

        // optional safety (avoids NPE if question is missing)
        if (question == null) {
            question = "";
        }

        String answer = aiService.askGemini(question, kpiData);

        Map<String, String> response = new HashMap<>();
        response.put("answer", answer);
        response.put("question", question);

        return response;
    }
}