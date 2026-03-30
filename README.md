# DevFlow — Developer Workflow Intelligence Platform

> A self-hostable analytics platform that connects to GitHub repositories, tracks PR activity, build health, and developer productivity — with an AI-powered natural language query interface.

**Live Demo:** https://devflow-dashboard.onrender.com

---

## Table of Contents

1. [What is DevFlow?](#what-is-devflow)
2. [Who is it for?](#who-is-it-for)
3. [How is it useful in real life?](#how-is-it-useful-in-real-life)
4. [How data gets in](#how-data-gets-in)
5. [Architecture](#architecture)
6. [Tech Stack](#tech-stack)
7. [Getting Started (Local)](#getting-started-local)
8. [Using the Dashboard](#using-the-dashboard)
9. [Adding Your Own Repo](#adding-your-own-repo)
10. [API Reference](#api-reference)
11. [Project Structure](#project-structure)
12. [Deployment](#deployment)
13. [Roadmap](#roadmap)

---

## What is DevFlow?

DevFlow is a platform that automatically collects data from GitHub repositories and gives you a real-time analytics dashboard for your engineering team. Think of it as **GitHub Insights but self-hosted, with a job scheduler and AI built in**.

Instead of manually checking GitHub every time someone asks *"how many PRs were merged last week?"* or *"who caused the build to break on Tuesday?"* — DevFlow answers those questions automatically.

You can even ask questions in plain English:

> *"Who is the most active developer this week?"*
> *"What is our build success rate?"*
> *"Who are the top 3 contributors?"*

And get real answers based on your actual data.

---

## Who is it for?

**Engineering Managers** who want visibility into their team's productivity without manually digging through GitHub.

**Developers** who want to understand their own contribution patterns and compare with teammates.

**Tech Leads** who need to track build health, PR review times, and bottlenecks in the development workflow.

**Students and Job Seekers** who want to demonstrate knowledge of distributed systems, microservices, Spring Boot, and cloud deployment in a portfolio project.

---

## How is it useful in real life?

Here are real scenarios where DevFlow saves time:

**Scenario 1 — Weekly team standup**
Instead of asking "what did everyone work on?", your manager opens DevFlow and sees exactly how many PRs each developer submitted, merged, and had reviewed.

**Scenario 2 — Build failures**
Your CI pipeline keeps failing. DevFlow shows which developer's commits triggered the failures and how often it happens — without anyone having to manually check GitHub Actions logs.

**Scenario 3 — PR bottlenecks**
PRs are piling up. DevFlow shows the average time from PR open to merge, and which PRs have been open the longest — so you know where the review bottleneck is.

**Scenario 4 — Team health reports**
Every Monday morning, DevFlow can automatically generate and send a team activity summary to your Slack channel — no manual work required.

---

## How data gets in

DevFlow pulls data from GitHub using two methods:

### 1. Manual ingestion (on demand)

Call the ingestion API with any public or private GitHub repo:

```bash
curl -X POST https://devflow-ingestion.onrender.com/api/ingest/repo \
  -H "Content-Type: application/json" \
  -d '{"owner":"microsoft","repo":"vscode"}'
```

This fetches the last 30 pull requests and saves them to the database.

### 2. Scheduled jobs (automatic)

The scheduler engine runs background jobs on a cron schedule. For example, a job can be set to sync PRs from your repo every 5 minutes automatically — no manual curl needed.

### 3. GitHub Webhooks (real-time) — coming soon

GitHub can send events to DevFlow in real time whenever a PR is opened, merged, or a build completes.

---

## Architecture

DevFlow is a monorepo containing 4 independent microservices that communicate via REST and a shared PostgreSQL database.

```
GitHub API
    │
    ▼
github-ingestion (port 8081)
    │ saves data to
    ▼
PostgreSQL Database
    │ reads data from
    ▼
analytics-service (port 8083) ◄──► Groq AI API
    │ serves data to
    ▼
React Frontend (port 5173)
    
scheduler-engine (port 8082)
    │ pushes jobs to
    ▼
Redis Queue
```

### Services

| Service | Port | What it does |
|---|---|---|
| `github-ingestion` | 8081 | Calls GitHub API, saves PR/build/commit data to PostgreSQL |
| `scheduler-engine` | 8082 | Runs cron jobs via Quartz Scheduler, pushes tasks to Redis |
| `analytics-service` | 8083 | Reads data from PostgreSQL, serves metrics API, handles AI queries |
| `frontend` | 5173 | React dashboard — charts, tables, leaderboard, AI query box |

---

## Tech Stack

| Layer | Technology |
|---|---|
| Backend | Spring Boot 3.5, Java 21 |
| Database | PostgreSQL 15 |
| Job Scheduler | Quartz Scheduler |
| Message Queue | Redis |
| Frontend | React 18, TypeScript, TailwindCSS, Recharts |
| AI Layer | Groq API (llama-3.1-8b-instant) |
| Containerization | Docker, Docker Compose |
| Deployment | Render |
| GitHub Integration | GitHub REST API v3 |

---

## Getting Started (Local)

### Prerequisites

Make sure you have these installed:

- Java 21+
- Maven 3.9+
- Docker Desktop
- Node.js 20+
- Git

### Step 1 — Clone the repo

```bash
git clone https://github.com/raj-deshmukh6403/devflow.git
cd devflow
```

### Step 2 — Start PostgreSQL and Redis

```bash
docker-compose up -d
```

This starts PostgreSQL on port 5432 and Redis on port 6379.

### Step 3 — Start the GitHub Ingestion Service

```bash
cd services/github-ingestion
# Add your GitHub token to application.yaml
./mvnw spring-boot:run
```

The service starts on port 8081.

### Step 4 — Start the Analytics Service

```bash
cd services/analytics-service
# Add your Groq API key to application.yaml
./mvnw spring-boot:run
```

The service starts on port 8083.

### Step 5 — Start the Scheduler Engine

```bash
cd services/scheduler-engine
./mvnw spring-boot:run
```

The service starts on port 8082.

### Step 6 — Start the Frontend

```bash
cd services/frontend
npm install
npm run dev
```

Open your browser at `http://localhost:5173`

### Step 7 — Ingest your first repo

```bash
curl -X POST http://localhost:8081/api/ingest/repo \
  -H "Content-Type: application/json" \
  -d '{"owner":"microsoft","repo":"vscode"}'
```

Refresh the dashboard and you will see real data!

---

## Using the Dashboard

### Dashboard Tab
Shows summary cards with total PRs, builds, build success rate and active developers. Also shows a bar chart of PR activity by date.

### Pull Requests Tab
Full table of all ingested pull requests with author avatars, PR title, date and status badge (open/closed).

### Leaderboard Tab
Developers ranked by PR activity. Top 3 get gold, silver and bronze medals.

### Ask AI Tab
Type any question in plain English about your team's data:

- *"Who is the most active developer?"*
- *"What is the build success rate?"*
- *"Who are the top 3 contributors?"*
- *"Give me a summary of team activity"*

The AI reads your actual data and gives a real answer.

---

## Adding Your Own Repo

You can add any public GitHub repo to DevFlow:

```bash
curl -X POST http://localhost:8081/api/ingest/repo \
  -H "Content-Type: application/json" \
  -d '{"owner":"YOUR_GITHUB_USERNAME","repo":"YOUR_REPO_NAME"}'
```

For private repos, make sure your GitHub token in `application.yaml` has `repo` scope.

You can ingest multiple repos — just call the endpoint multiple times with different owner/repo combinations.

---

## API Reference

### Ingestion Service (port 8081)

| Method | Endpoint | Description |
|---|---|---|
| POST | `/api/ingest/repo` | Ingest PRs from a GitHub repo |

### Analytics Service (port 8083)

| Method | Endpoint | Description |
|---|---|---|
| GET | `/api/metrics/summary` | Total PRs, builds, success rate |
| GET | `/api/developers/leaderboard` | Developers ranked by PR count |
| GET | `/api/metrics/prs?from=&to=` | PRs in a date range |
| GET | `/api/metrics/builds?from=&to=` | Builds in a date range |
| POST | `/api/ai/query` | Ask an AI question about your data |

### Scheduler Service (port 8082)

| Method | Endpoint | Description |
|---|---|---|
| GET | `/api/scheduler/jobs` | List all scheduled jobs |
| POST | `/api/scheduler/jobs` | Create a new scheduled job |
| POST | `/api/scheduler/jobs/{id}/trigger` | Manually trigger a job |
| GET | `/api/scheduler/jobs/{id}/history` | View job execution history |

---

## Project Structure

```
devflow/
├── docker-compose.yml          ← run everything locally
├── scripts/
│   └── setup-db.sql            ← database schema
└── services/
    ├── github-ingestion/       ← fetches data from GitHub API
    ├── scheduler-engine/       ← Quartz cron jobs + Redis queue
    ├── analytics-service/      ← metrics API + AI query layer
    └── frontend/               ← React dashboard
```

---

## Deployment

The project is deployed on Render using Docker:

- **Analytics API:** https://devflow-analytics.onrender.com
- **Ingestion API:** https://devflow-ingestion.onrender.com
- **Dashboard:** https://devflow-dashboard.onrender.com

To deploy your own instance:

1. Fork this repo on GitHub
2. Create a PostgreSQL database on Render
3. Deploy each service as a Web Service on Render pointing to the correct `Root Directory`
4. Set environment variables: `SPRING_DATASOURCE_URL`, `SPRING_DATASOURCE_USERNAME`, `SPRING_DATASOURCE_PASSWORD`, `GITHUB_TOKEN`, `GROQ_API_KEY`
5. Deploy the frontend as a Static Site with `VITE_API_URL` pointing to your analytics service

---

## Roadmap

- [x] GitHub PR ingestion
- [x] Quartz job scheduler
- [x] Redis job queue
- [x] Analytics REST API
- [x] React dashboard with charts
- [x] Developer leaderboard
- [x] AI natural language queries
- [x] Render deployment
- [ ] GitHub Webhooks for real-time updates
- [ ] Build data ingestion from GitHub Actions
- [ ] Commit data ingestion
- [ ] JWT authentication
- [ ] API Gateway service
- [ ] Worker node for executing scheduled jobs
- [ ] Slack/email notifications
- [ ] Weekly automated team reports
- [ ] Kubernetes deployment

---

## Environment Variables

| Variable | Service | Description |
|---|---|---|
| `GITHUB_TOKEN` | github-ingestion | GitHub Personal Access Token with `repo` scope |
| `GROQ_API_KEY` | analytics-service | Groq API key from console.groq.com |
| `SPRING_DATASOURCE_URL` | all backend services | PostgreSQL JDBC URL |
| `SPRING_DATASOURCE_USERNAME` | all backend services | PostgreSQL username |
| `SPRING_DATASOURCE_PASSWORD` | all backend services | PostgreSQL password |
| `VITE_API_URL` | frontend | Base URL of the analytics service |

---

## Contributing

Pull requests are welcome! If you want to add a feature from the roadmap, open an issue first to discuss it.

---

## License

MIT License — free to use, modify and deploy for personal or commercial projects.

---

*Built with Spring Boot, React, PostgreSQL, Redis, Quartz Scheduler and Groq AI.*