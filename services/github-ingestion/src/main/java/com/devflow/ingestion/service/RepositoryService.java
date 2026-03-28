package com.devflow.ingestion.service;

import com.devflow.ingestion.model.Repository;
import com.devflow.ingestion.repository.GithubRepositoryRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.time.OffsetDateTime;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class RepositoryService {

    private final GithubRepositoryRepo repositoryRepo;

    public Repository findOrCreate(Long githubId, String owner, String name, boolean isPrivate) {
        Optional<Repository> existing = repositoryRepo.findByGithubId(githubId);
        if (existing.isPresent()) {
            log.debug("Repository already exists: {}/{}", owner, name);
            return existing.get();
        }

        Repository repo = new Repository();
        repo.setGithubId(githubId);
        repo.setOwner(owner);
        repo.setName(name);
        repo.setPrivate_(isPrivate);
        repo.setCreatedAt(OffsetDateTime.now());

        log.info("Saving new repository: {}/{}", owner, name);
        return repositoryRepo.save(repo);
    }
}