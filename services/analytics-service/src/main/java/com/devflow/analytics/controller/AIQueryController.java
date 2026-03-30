package com.devflow.analytics.controller;

import com.devflow.analytics.service.AIQueryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/ai")
@RequiredArgsConstructor
public class AIQueryController {

    private final AIQueryService aiQueryService;

    @PostMapping("/query")
    public ResponseEntity<Map<String, String>> query(@RequestBody Map<String, String> request) {
        String question = request.get("question");
        log.info("AI query received: {}", question);
        String answer = aiQueryService.query(question);
        return ResponseEntity.ok(Map.of(
                "question", question,
                "answer", answer
        ));
    }
}