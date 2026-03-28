package com.devflow.ingestion.service;

import com.devflow.ingestion.model.Developer;
import com.devflow.ingestion.repository.DeveloperRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.time.OffsetDateTime;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class DeveloperService {

    private final DeveloperRepository developerRepository;

    public Developer findOrCreate(String githubLogin, String displayName, String avatarUrl) {
        Optional<Developer> existing = developerRepository.findByGithubLogin(githubLogin);
        if (existing.isPresent()) {
            log.debug("Developer already exists: {}", githubLogin);
            return existing.get();
        }

        Developer developer = new Developer();
        developer.setGithubLogin(githubLogin);
        developer.setDisplayName(displayName);
        developer.setAvatarUrl(avatarUrl);
        developer.setCreatedAt(OffsetDateTime.now());

        log.info("Saving new developer: {}", githubLogin);
        return developerRepository.save(developer);
    }
}