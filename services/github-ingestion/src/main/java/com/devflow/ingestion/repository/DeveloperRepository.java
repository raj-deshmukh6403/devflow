package com.devflow.ingestion.repository;

import com.devflow.ingestion.model.Developer;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.UUID;

public interface DeveloperRepository extends JpaRepository<Developer, UUID> {
    Optional<Developer> findByGithubLogin(String githubLogin);
}