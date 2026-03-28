package com.devflow.scheduler.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class JobQueueService {

    private final RedisTemplate<String, String> redisTemplate;

    private static final String QUEUE_PREFIX = "job:";

    public void enqueue(String jobType, String payload) {
        String queueKey = QUEUE_PREFIX + jobType;
        redisTemplate.opsForList().leftPush(queueKey, payload);
        log.info("Enqueued job type={} payload={}", jobType, payload);
    }

    public String dequeue(String jobType) {
        String queueKey = QUEUE_PREFIX + jobType;
        return redisTemplate.opsForList().rightPop(queueKey);
    }

    public Long getQueueDepth(String jobType) {
        String queueKey = QUEUE_PREFIX + jobType;
        return redisTemplate.opsForList().size(queueKey);
    }
}