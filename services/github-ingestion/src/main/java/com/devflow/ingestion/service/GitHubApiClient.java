package com.devflow.ingestion.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class GitHubApiClient {

    private final RestTemplate restTemplate;

    @Value("${github.token:}")
    private String githubToken;

    private static final String GITHUB_API_URL = "https://api.github.com";

    public GitHubApiClient() {
        this.restTemplate = new RestTemplate();
    }

    private HttpHeaders buildHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", "application/vnd.github.v3+json");
        if (githubToken != null && !githubToken.isEmpty()) {
            headers.set("Authorization", "Bearer " + githubToken);
        }
        return headers;
    }

    public List<Map<String, Object>> fetchPullRequests(String owner, String repo, String state) {
        String url = GITHUB_API_URL + "/repos/" + owner + "/" + repo + "/pulls?state=" + state + "&per_page=30";
        log.info("Fetching PRs from: {}", url);

        HttpEntity<Void> entity = new HttpEntity<>(buildHeaders());
        ResponseEntity<List> response = restTemplate.exchange(url, HttpMethod.GET, entity, List.class);

        log.info("Fetched {} PRs for {}/{}", response.getBody().size(), owner, repo);
        return response.getBody();
    }

    public Map<String, Object> fetchRepository(String owner, String repo) {
        String url = GITHUB_API_URL + "/repos/" + owner + "/" + repo;
        log.info("Fetching repo info from: {}", url);

        HttpEntity<Void> entity = new HttpEntity<>(buildHeaders());
        ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.GET, entity, Map.class);

        return response.getBody();
    }
}