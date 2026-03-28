package com.devflow.ingestion.repository;

import com.devflow.ingestion.model.Commit;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.UUID;

public interface CommitRepository extends JpaRepository<Commit, UUID> {
    Optional<Commit> findBySha(String sha);
    boolean existsBySha(String sha);
}