package com.devflow.ingestion.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.OffsetDateTime;
import java.util.UUID;

@Data
@Entity
@Table(name = "commits")
public class Commit {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "repo_id")
    private Repository repository;

    @Column(unique = true, nullable = false)
    private String sha;

    @ManyToOne
    @JoinColumn(name = "author_id")
    private Developer author;

    @Column
    private String message;

    @Column(name = "files_changed")
    private Integer filesChanged;

    @Column(name = "committed_at", nullable = false)
    private OffsetDateTime committedAt;
}