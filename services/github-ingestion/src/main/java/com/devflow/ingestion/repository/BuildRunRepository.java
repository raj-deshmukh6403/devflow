package com.devflow.ingestion.repository;

import com.devflow.ingestion.model.BuildRun;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface BuildRunRepository extends JpaRepository<BuildRun, UUID> {
    List<BuildRun> findByRepositoryId(UUID repoId);
    boolean existsByGithubRunId(Long githubRunId);
}