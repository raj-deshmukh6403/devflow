package com.devflow.analytics.service;

import com.devflow.analytics.model.Developer;
import com.devflow.analytics.repository.BuildRunRepository;
import com.devflow.analytics.repository.DeveloperRepository;
import com.devflow.analytics.repository.PullRequestRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class DeveloperStatsService {

    private final DeveloperRepository developerRepository;
    private final PullRequestRepository pullRequestRepository;
    private final BuildRunRepository buildRunRepository;

    public List<Map<String, Object>> getLeaderboard() {
        List<Developer> developers = developerRepository.findAll();
        List<Object[]> prCounts = pullRequestRepository.countPRsByDeveloper();

        Map<String, Long> prCountMap = new HashMap<>();
        for (Object[] row : prCounts) {
            prCountMap.put((String) row[0], (Long) row[1]);
        }

        List<Map<String, Object>> leaderboard = new ArrayList<>();
        for (Developer dev : developers) {
            Map<String, Object> entry = new HashMap<>();
            entry.put("githubLogin", dev.getGithubLogin());
            entry.put("displayName", dev.getDisplayName());
            entry.put("avatarUrl", dev.getAvatarUrl());
            entry.put("totalPRs", prCountMap.getOrDefault(dev.getGithubLogin(), 0L));
            leaderboard.add(entry);
        }

        leaderboard.sort((a, b) ->
                Long.compare((Long) b.get("totalPRs"), (Long) a.get("totalPRs")));

        log.debug("Leaderboard generated for {} developers", leaderboard.size());
        return leaderboard;
    }

    public Map<String, Object> getDeveloperStats(String githubLogin) {
        Developer developer = developerRepository.findByGithubLogin(githubLogin)
                .orElseThrow(() -> new RuntimeException("Developer not found: " + githubLogin));

        long totalPRs = pullRequestRepository.countPRsByDeveloper()
                .stream()
                .filter(row -> githubLogin.equals(row[0]))
                .mapToLong(row -> (Long) row[1])
                .sum();

        Map<String, Object> stats = new HashMap<>();
        stats.put("developer", developer);
        stats.put("totalPRs", totalPRs);
        return stats;
    }
}