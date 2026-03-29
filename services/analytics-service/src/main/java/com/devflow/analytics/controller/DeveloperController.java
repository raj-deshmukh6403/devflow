package com.devflow.analytics.controller;

import com.devflow.analytics.service.DeveloperStatsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/developers")
@RequiredArgsConstructor
public class DeveloperController {

    private final DeveloperStatsService developerStatsService;

    @GetMapping("/leaderboard")
    public ResponseEntity<List<Map<String, Object>>> getLeaderboard() {
        return ResponseEntity.ok(developerStatsService.getLeaderboard());
    }

    @GetMapping("/{githubLogin}")
    public ResponseEntity<Map<String, Object>> getDeveloperStats(
            @PathVariable String githubLogin) {
        return ResponseEntity.ok(developerStatsService.getDeveloperStats(githubLogin));
    }
}