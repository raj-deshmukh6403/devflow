package com.devflow.analytics.service;

import com.devflow.analytics.model.BuildRun;
import com.devflow.analytics.model.PullRequest;
import com.devflow.analytics.repository.BuildRunRepository;
import com.devflow.analytics.repository.PullRequestRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class MetricsService {

    private final PullRequestRepository pullRequestRepository;
    private final BuildRunRepository buildRunRepository;

    public Map<String, Object> getSummaryMetrics() {
        long totalPRs = pullRequestRepository.count();
        long totalBuilds = buildRunRepository.count();

        List<Object[]> buildStatusCounts = buildRunRepository.countByStatus();
        long successBuilds = 0;
        long failedBuilds = 0;
        for (Object[] row : buildStatusCounts) {
            String status = (String) row[0];
            Long count = (Long) row[1];
            if ("success".equalsIgnoreCase(status)) successBuilds = count;
            if ("failure".equalsIgnoreCase(status)) failedBuilds = count;
        }

        double successRate = totalBuilds > 0
                ? (double) successBuilds / totalBuilds * 100 : 0;

        Map<String, Object> metrics = new HashMap<>();
        metrics.put("totalPRs", totalPRs);
        metrics.put("totalBuilds", totalBuilds);
        metrics.put("successBuilds", successBuilds);
        metrics.put("failedBuilds", failedBuilds);
        metrics.put("buildSuccessRate", Math.round(successRate * 100.0) / 100.0);
        metrics.put("totalDevelopers", 0);

        log.debug("Summary metrics: {}", metrics);
        return metrics;
    }

    public List<PullRequest> getPRsByDateRange(OffsetDateTime from, OffsetDateTime to) {
        return pullRequestRepository.findByDateRange(from, to);
    }

    public List<BuildRun> getBuildsByDateRange(OffsetDateTime from, OffsetDateTime to) {
        return buildRunRepository.findByDateRange(from, to);
    }

    public List<Object[]> getPRCountByDeveloper() {
        return pullRequestRepository.countPRsByDeveloper();
    }
}