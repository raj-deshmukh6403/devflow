package com.devflow.scheduler.controller;

import com.devflow.scheduler.model.JobDefinition;
import com.devflow.scheduler.model.JobExecution;
import com.devflow.scheduler.service.JobQueueService;
import com.devflow.scheduler.service.JobService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/scheduler")
@RequiredArgsConstructor
public class JobController {

    private final JobService jobService;
    private final JobQueueService jobQueueService;

    @GetMapping("/jobs")
    public ResponseEntity<List<JobDefinition>> getAllJobs() {
        return ResponseEntity.ok(jobService.getAllJobs());
    }

    @PostMapping("/jobs")
    public ResponseEntity<JobDefinition> createJob(@RequestBody Map<String, String> request) {
        JobDefinition job = jobService.createJob(
                request.get("name"),
                request.get("cronExpr"),
                request.get("jobType"),
                request.get("config")
        );
        return ResponseEntity.ok(job);
    }

    @PostMapping("/jobs/{id}/trigger")
    public ResponseEntity<JobExecution> triggerJob(@PathVariable UUID id) {
        return ResponseEntity.ok(jobService.triggerJob(id));
    }

    @GetMapping("/jobs/{id}/history")
    public ResponseEntity<List<JobExecution>> getJobHistory(@PathVariable UUID id) {
        return ResponseEntity.ok(jobService.getJobHistory(id));
    }

    @GetMapping("/queue/{jobType}/depth")
    public ResponseEntity<Map<String, Object>> getQueueDepth(@PathVariable String jobType) {
        Long depth = jobQueueService.getQueueDepth(jobType);
        return ResponseEntity.ok(Map.of("jobType", jobType, "depth", depth));
    }
}