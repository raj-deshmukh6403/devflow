-- Enable UUID generation
CREATE EXTENSION IF NOT EXISTS "pgcrypto";

-- Repositories
CREATE TABLE IF NOT EXISTS repositories (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    github_id BIGINT UNIQUE NOT NULL,
    owner VARCHAR(255) NOT NULL,
    name VARCHAR(255) NOT NULL,
    private BOOLEAN DEFAULT false,
    created_at TIMESTAMPTZ DEFAULT NOW()
);

-- Developers
CREATE TABLE IF NOT EXISTS developers (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    github_login VARCHAR(255) UNIQUE NOT NULL,
    display_name VARCHAR(255),
    avatar_url TEXT,
    created_at TIMESTAMPTZ DEFAULT NOW()
);

-- Pull Requests
CREATE TABLE IF NOT EXISTS pull_requests (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    repo_id UUID REFERENCES repositories(id),
    github_pr_id BIGINT NOT NULL,
    author_id UUID REFERENCES developers(id),
    title TEXT,
    state VARCHAR(20),
    created_at TIMESTAMPTZ NOT NULL,
    merged_at TIMESTAMPTZ,
    closed_at TIMESTAMPTZ,
    time_to_merge INTERVAL
);

-- Build Runs
CREATE TABLE IF NOT EXISTS build_runs (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    repo_id UUID REFERENCES repositories(id),
    github_run_id BIGINT NOT NULL,
    triggered_by UUID REFERENCES developers(id),
    workflow_name VARCHAR(255),
    status VARCHAR(20),
    started_at TIMESTAMPTZ NOT NULL,
    completed_at TIMESTAMPTZ,
    duration_ms BIGINT,
    error_message TEXT
);

-- Commits
CREATE TABLE IF NOT EXISTS commits (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    repo_id UUID REFERENCES repositories(id),
    sha VARCHAR(40) UNIQUE NOT NULL,
    author_id UUID REFERENCES developers(id),
    message TEXT,
    files_changed INT,
    committed_at TIMESTAMPTZ NOT NULL
);

-- Repo Metrics (TimescaleDB hypertable)
CREATE TABLE IF NOT EXISTS repo_metrics (
    time TIMESTAMPTZ NOT NULL,
    repo_id UUID NOT NULL,
    prs_opened INT DEFAULT 0,
    prs_merged INT DEFAULT 0,
    builds_total INT DEFAULT 0,
    builds_failed INT DEFAULT 0,
    commits_total INT DEFAULT 0
);

SELECT create_hypertable('repo_metrics', 'time', if_not_exists => TRUE);

-- Job Definitions
CREATE TABLE IF NOT EXISTS job_definitions (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name VARCHAR(255) NOT NULL,
    cron_expr VARCHAR(100),
    job_type VARCHAR(100) NOT NULL,
    config JSONB,
    enabled BOOLEAN DEFAULT true,
    created_at TIMESTAMPTZ DEFAULT NOW()
);

-- Job Executions
CREATE TABLE IF NOT EXISTS job_executions (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    job_id UUID REFERENCES job_definitions(id),
    status VARCHAR(20),
    started_at TIMESTAMPTZ,
    completed_at TIMESTAMPTZ,
    attempt INT DEFAULT 1,
    error_log TEXT,
    result_json JSONB
);