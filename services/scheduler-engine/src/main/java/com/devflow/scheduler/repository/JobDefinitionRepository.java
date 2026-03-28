package com.devflow.scheduler.repository;

import com.devflow.scheduler.model.JobDefinition;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface JobDefinitionRepository extends JpaRepository<JobDefinition, UUID> {
    List<JobDefinition> findByEnabled(Boolean enabled);
    boolean existsByName(String name);
}