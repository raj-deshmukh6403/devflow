package com.devflow.ingestion.controller;

import com.devflow.ingestion.model.PullRequest;
import com.devflow.ingestion.model.Repository;
import com.devflow.ingestion.service.GitHubApiClient;
import com.devflow.ingestion.service.PRIngestionService;
import com.devflow.ingestion.service.RepositoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/ingest")
@RequiredArgsConstructor
public class IngestionController {

    private final GitHubApiClient gitHubApiClient;
    private final RepositoryService repositoryService;
    private final PRIngestionService prIngestionService;

    @PostMapping("/repo")
    public ResponseEntity<Map<String, Object>> ingestRepository(@RequestBody Map<String, String> request) {
        String owner = request.get("owner");
        String repo = request.get("repo");

        log.info("Starting ingestion for {}/{}", owner, repo);

        Map<String, Object> repoData = gitHubApiClient.fetchRepository(owner, repo);

        Repository repository = repositoryService.findOrCreate(
                Long.valueOf(repoData.get("id").toString()),
                owner,
                repo,
                (Boolean) repoData.get("private")
        );

        List<Map<String, Object>> prs = gitHubApiClient.fetchPullRequests(owner, repo, "all");
        int saved = 0;
        for (Map<String, Object> prData : prs) {
            PullRequest pr = prIngestionService.ingestPR(repository, prData);
            if (pr != null) saved++;
        }

        return ResponseEntity.ok(Map.of(
                "repository", owner + "/" + repo,
                "prs_fetched", prs.size(),
                "prs_saved", saved
        ));
    }

    @GetMapping("/repo/{owner}/{repo}/prs")
    public ResponseEntity<List<PullRequest>> getPRs(@PathVariable String owner, @PathVariable String repo) {
        Map<String, Object> repoData = gitHubApiClient.fetchRepository(owner, repo);
        Repository repository = repositoryService.findOrCreate(
                Long.valueOf(repoData.get("id").toString()),
                owner,
                repo,
                (Boolean) repoData.get("private")
        );
        List<PullRequest> prs = prIngestionService.getPRsForRepo(repository.getId());
        return ResponseEntity.ok(prs);
    }
}