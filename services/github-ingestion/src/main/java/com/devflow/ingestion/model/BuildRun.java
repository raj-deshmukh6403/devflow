package com.devflow.ingestion.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.OffsetDateTime;
import java.util.UUID;

@Data
@Entity
@Table(name = "build_runs")
public class BuildRun {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "repo_id")
    private Repository repository;

    @Column(name = "github_run_id", nullable = false)
    private Long githubRunId;

    @ManyToOne
    @JoinColumn(name = "triggered_by")
    private Developer triggeredBy;

    @Column(name = "workflow_name")
    private String workflowName;

    @Column
    private String status;

    @Column(name = "started_at", nullable = false)
    private OffsetDateTime startedAt;

    @Column(name = "completed_at")
    private OffsetDateTime completedAt;

    @Column(name = "duration_ms")
    private Long durationMs;

    @Column(name = "error_message")
    private String errorMessage;
}