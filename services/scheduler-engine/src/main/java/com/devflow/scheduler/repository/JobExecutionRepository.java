package com.devflow.scheduler.repository;

import com.devflow.scheduler.model.JobExecution;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface JobExecutionRepository extends JpaRepository<JobExecution, UUID> {
    List<JobExecution> findByJobDefinitionIdOrderByStartedAtDesc(UUID jobId);
    List<JobExecution> findByStatus(String status);
}