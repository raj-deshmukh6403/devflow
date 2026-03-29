package com.devflow.analytics.controller;

import com.devflow.analytics.model.BuildRun;
import com.devflow.analytics.model.PullRequest;
import com.devflow.analytics.service.MetricsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/metrics")
@RequiredArgsConstructor
public class MetricsController {

    private final MetricsService metricsService;

    @GetMapping("/summary")
    public ResponseEntity<Map<String, Object>> getSummary() {
        return ResponseEntity.ok(metricsService.getSummaryMetrics());
    }

    @GetMapping("/prs")
    public ResponseEntity<List<PullRequest>> getPRs(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) OffsetDateTime from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) OffsetDateTime to) {
        return ResponseEntity.ok(metricsService.getPRsByDateRange(from, to));
    }

    @GetMapping("/builds")
    public ResponseEntity<List<BuildRun>> getBuilds(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) OffsetDateTime from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) OffsetDateTime to) {
        return ResponseEntity.ok(metricsService.getBuildsByDateRange(from, to));
    }

    @GetMapping("/prs/by-developer")
    public ResponseEntity<List<Object[]>> getPRsByDeveloper() {
        return ResponseEntity.ok(metricsService.getPRCountByDeveloper());
    }
}