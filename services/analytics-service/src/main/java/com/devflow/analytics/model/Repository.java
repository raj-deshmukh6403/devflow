package com.devflow.analytics.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.OffsetDateTime;
import java.util.UUID;

@Data
@Entity
@Table(name = "repositories")
public class Repository {

    @Id
    private UUID id;

    @Column(name = "github_id")
    private Long githubId;

    @Column
    private String owner;

    @Column
    private String name;

    @Column(name = "created_at")
    private OffsetDateTime createdAt;
}
