package com.devflow.scheduler.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.OffsetDateTime;
import java.util.UUID;

@Data
@Entity
@Table(name = "job_definitions")
public class JobDefinition {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(name = "cron_expr")
    private String cronExpr;

    @Column(name = "job_type", nullable = false)
    private String jobType;

    @Column(columnDefinition = "jsonb")
    @org.hibernate.annotations.JdbcTypeCode(org.hibernate.type.SqlTypes.JSON)
    private String config;

    @Column
    private Boolean enabled = true;

    @Column(name = "created_at")
    private OffsetDateTime createdAt;
}