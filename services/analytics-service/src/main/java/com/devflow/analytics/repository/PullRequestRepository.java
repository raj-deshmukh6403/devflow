package com.devflow.analytics.repository;

import com.devflow.analytics.model.PullRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

public interface PullRequestRepository extends JpaRepository<PullRequest, UUID> {

    List<PullRequest> findByRepositoryId(UUID repoId);

    List<PullRequest> findByState(String state);

    @Query("SELECT p FROM PullRequest p WHERE p.createdAt >= :from AND p.createdAt <= :to")
    List<PullRequest> findByDateRange(
            @Param("from") OffsetDateTime from,
            @Param("to") OffsetDateTime to
    );

    @Query("SELECT p.author.githubLogin, COUNT(p) FROM PullRequest p GROUP BY p.author.githubLogin ORDER BY COUNT(p) DESC")
    List<Object[]> countPRsByDeveloper();
}