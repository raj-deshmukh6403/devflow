package com.devflow.ingestion.repository;

import com.devflow.ingestion.model.PullRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface PullRequestRepository extends JpaRepository<PullRequest, UUID> {
    List<PullRequest> findByRepositoryId(UUID repoId);
    boolean existsByGithubPrId(Long githubPrId);
}