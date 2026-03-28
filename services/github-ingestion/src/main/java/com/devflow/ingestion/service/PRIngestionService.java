package com.devflow.ingestion.service;

import com.devflow.ingestion.model.Developer;
import com.devflow.ingestion.model.PullRequest;
import com.devflow.ingestion.model.Repository;
import com.devflow.ingestion.repository.PullRequestRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class PRIngestionService {

    private final PullRequestRepository pullRequestRepository;
    private final DeveloperService developerService;

    public PullRequest ingestPR(Repository repository, Map<String, Object> prData) {
        Long githubPrId = Long.valueOf(prData.get("number").toString());

        if (pullRequestRepository.existsByGithubPrId(githubPrId)) {
            log.debug("PR already exists: #{}", githubPrId);
            return null;
        }

        Map<String, Object> authorData = (Map<String, Object>) prData.get("user");
        Developer author = developerService.findOrCreate(
                authorData.get("login").toString(),
                authorData.get("login").toString(),
                authorData.get("avatar_url").toString()
        );

        PullRequest pr = new PullRequest();
        pr.setRepository(repository);
        pr.setGithubPrId(githubPrId);
        pr.setAuthor(author);
        pr.setTitle(prData.get("title").toString());
        pr.setState(prData.get("state").toString());
        pr.setCreatedAt(OffsetDateTime.parse(prData.get("created_at").toString()));

        if (prData.get("merged_at") != null) {
            pr.setMergedAt(OffsetDateTime.parse(prData.get("merged_at").toString()));
        }
        if (prData.get("closed_at") != null) {
            pr.setClosedAt(OffsetDateTime.parse(prData.get("closed_at").toString()));
        }

        log.info("Saving PR #{}: {}", githubPrId, pr.getTitle());
        return pullRequestRepository.save(pr);
    }

    public List<PullRequest> getPRsForRepo(java.util.UUID repoId) {
        return pullRequestRepository.findByRepositoryId(repoId);
    }
}