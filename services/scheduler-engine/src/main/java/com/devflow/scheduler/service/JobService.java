package com.devflow.scheduler.service;

import com.devflow.scheduler.model.JobDefinition;
import com.devflow.scheduler.model.JobExecution;
import com.devflow.scheduler.repository.JobDefinitionRepository;
import com.devflow.scheduler.repository.JobExecutionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class JobService {

    private final JobDefinitionRepository jobDefinitionRepository;
    private final JobExecutionRepository jobExecutionRepository;
    private final JobQueueService jobQueueService;

    public JobDefinition createJob(String name, String cronExpr, String jobType, String config) {
        if (jobDefinitionRepository.existsByName(name)) {
            throw new RuntimeException("Job with name '" + name + "' already exists");
        }

        JobDefinition job = new JobDefinition();
        job.setName(name);
        job.setCronExpr(cronExpr);
        job.setJobType(jobType);
        job.setConfig(config);
        job.setEnabled(true);
        job.setCreatedAt(OffsetDateTime.now());

        log.info("Creating job: {}", name);
        return jobDefinitionRepository.save(job);
    }

    public List<JobDefinition> getAllJobs() {
        return jobDefinitionRepository.findAll();
    }

    public List<JobExecution> getJobHistory(UUID jobId) {
        return jobExecutionRepository.findByJobDefinitionIdOrderByStartedAtDesc(jobId);
    }

    public JobExecution triggerJob(UUID jobId) {
        JobDefinition job = jobDefinitionRepository.findById(jobId)
                .orElseThrow(() -> new RuntimeException("Job not found: " + jobId));

        JobExecution execution = new JobExecution();
        execution.setJobDefinition(job);
        execution.setStatus("QUEUED");
        execution.setStartedAt(OffsetDateTime.now());
        execution.setAttempt(1);
        JobExecution saved = jobExecutionRepository.save(execution);

        jobQueueService.enqueue(job.getJobType(),
                "{\"jobId\":\"" + jobId + "\",\"executionId\":\"" + saved.getId() + "\"}");

        log.info("Triggered job: {} executionId: {}", job.getName(), saved.getId());
        return saved;
    }
}