package com.devflow.ingestion.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.OffsetDateTime;
import java.util.UUID;

@Data
@Entity
@Table(name = "repositories")
public class Repository {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "github_id", unique = true, nullable = false)
    private Long githubId;

    @Column(nullable = false)
    private String owner;

    @Column(nullable = false)
    private String name;

    @Column(name = "private")
    private Boolean private_ = false;

    @Column(name = "created_at")
    private OffsetDateTime createdAt;
}