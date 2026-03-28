package com.devflow.scheduler.jobs;

import com.devflow.scheduler.service.JobQueueService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class PRSyncJob implements Job {

    private final JobQueueService jobQueueService;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        log.info("PRSyncJob triggered by Quartz scheduler");
        String payload = "{\"jobType\":\"PR_SYNC\",\"triggeredAt\":\""
                + java.time.OffsetDateTime.now() + "\"}";
        jobQueueService.enqueue("PR_SYNC", payload);
        log.info("PRSyncJob enqueued payload to Redis");
    }
}