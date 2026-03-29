package com.devflow.analytics.repository;

import com.devflow.analytics.model.BuildRun;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

public interface BuildRunRepository extends JpaRepository<BuildRun, UUID> {

    List<BuildRun> findByRepositoryId(UUID repoId);

    List<BuildRun> findByStatus(String status);

    @Query("SELECT b FROM BuildRun b WHERE b.startedAt >= :from AND b.startedAt <= :to")
    List<BuildRun> findByDateRange(
            @Param("from") OffsetDateTime from,
            @Param("to") OffsetDateTime to
    );

    @Query("SELECT b.status, COUNT(b) FROM BuildRun b GROUP BY b.status")
    List<Object[]> countByStatus();
}