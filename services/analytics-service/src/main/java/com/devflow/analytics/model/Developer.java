package com.devflow.analytics.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.OffsetDateTime;
import java.util.UUID;

@Data
@Entity
@Table(name = "developers")
public class Developer {

    @Id
    private UUID id;

    @Column(name = "github_login")
    private String githubLogin;

    @Column(name = "display_name")
    private String displayName;

    @Column(name = "avatar_url")
    private String avatarUrl;

    @Column(name = "created_at")
    private OffsetDateTime createdAt;
}