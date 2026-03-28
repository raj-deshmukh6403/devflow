package com.devflow.ingestion.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.OffsetDateTime;
import java.util.UUID;

@Data
@Entity
@Table(name = "pull_requests")
public class PullRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "repo_id")
    private Repository repository;

    @Column(name = "github_pr_id", nullable = false)
    private Long githubPrId;

    @ManyToOne
    @JoinColumn(name = "author_id")
    private Developer author;

    @Column
    private String title;

    @Column
    private String state;

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;

    @Column(name = "merged_at")
    private OffsetDateTime mergedAt;

    @Column(name = "closed_at")
    private OffsetDateTime closedAt;
}