package com.devflow.ingestion.repository;

import com.devflow.ingestion.model.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.UUID;

public interface GithubRepositoryRepo extends JpaRepository<Repository, UUID> {
    Optional<Repository> findByGithubId(Long githubId);
    Optional<Repository> findByOwnerAndName(String owner, String name);
}