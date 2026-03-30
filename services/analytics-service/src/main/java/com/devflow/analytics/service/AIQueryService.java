package com.devflow.analytics.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class AIQueryService {

    @Value("${groq.api.key:}")
    private String groqApiKey;

    private static final String GROQ_URL = "https://api.groq.com/openai/v1/chat/completions";

    private final MetricsService metricsService;
    private final DeveloperStatsService developerStatsService;

    public String query(String userQuestion) {
        Map<String, Object> summary = metricsService.getSummaryMetrics();
        List<Map<String, Object>> leaderboard = developerStatsService.getLeaderboard();

        StringBuilder context = new StringBuilder();
        context.append("Here is the current developer workflow data:\n\n");
        context.append("Summary: ").append(summary).append("\n\n");
        context.append("Developer Leaderboard (ranked by PRs):\n");
        for (int i = 0; i < Math.min(leaderboard.size(), 10); i++) {
            Map<String, Object> dev = leaderboard.get(i);
            context.append((i + 1)).append(". ")
                    .append(dev.get("githubLogin"))
                    .append(" - ").append(dev.get("totalPRs")).append(" PRs\n");
        }

        String systemPrompt = "You are a developer analytics assistant. " +
                "Answer questions about the engineering team based on the data provided. " +
                "Be concise and helpful. Here is the data:\n\n" + context;

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + groqApiKey);
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> message1 = new HashMap<>();
        message1.put("role", "system");
        message1.put("content", systemPrompt);

        Map<String, Object> message2 = new HashMap<>();
        message2.put("role", "user");
        message2.put("content", userQuestion);

        Map<String, Object> body = new HashMap<>();
        body.put("model", "llama3-8b-8192");
        body.put("messages", List.of(message1, message2));
        body.put("max_tokens", 500);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);

        try {
            ResponseEntity<Map> response = restTemplate.exchange(
                    GROQ_URL, HttpMethod.POST, entity, Map.class);
            List<Map<String, Object>> choices = (List<Map<String, Object>>) response.getBody().get("choices");
            Map<String, Object> choice = choices.get(0);
            Map<String, Object> msg = (Map<String, Object>) choice.get("message");
            return msg.get("content").toString();
        } catch (Exception e) {
            log.error("Groq API error: {}", e.getMessage());
            return "Sorry I could not process your question right now.";
        }
    }
}