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
public class WeeklyReportJob implements Job {

    private final JobQueueService jobQueueService;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        log.info("WeeklyReportJob triggered by Quartz scheduler");
        String payload = "{\"jobType\":\"WEEKLY_REPORT\",\"triggeredAt\":\""
                + java.time.OffsetDateTime.now() + "\"}";
        jobQueueService.enqueue("WEEKLY_REPORT", payload);
        log.info("WeeklyReportJob enqueued payload to Redis");
    }
}