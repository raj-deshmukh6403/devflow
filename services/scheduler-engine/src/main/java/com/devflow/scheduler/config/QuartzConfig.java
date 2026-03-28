package com.devflow.scheduler.config;

import com.devflow.scheduler.jobs.PRSyncJob;
import com.devflow.scheduler.jobs.WeeklyReportJob;
import org.quartz.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class QuartzConfig {

    @Bean
    public JobDetail prSyncJobDetail() {
        return JobBuilder.newJob(PRSyncJob.class)
                .withIdentity("PRSyncJob")
                .withDescription("Syncs PRs from GitHub every 5 minutes")
                .storeDurably()
                .build();
    }

    @Bean
    public Trigger prSyncTrigger(JobDetail prSyncJobDetail) {
        return TriggerBuilder.newTrigger()
                .forJob(prSyncJobDetail)
                .withIdentity("PRSyncTrigger")
                .withSchedule(CronScheduleBuilder.cronSchedule("0 0/5 * * * ?"))
                .build();
    }

    @Bean
    public JobDetail weeklyReportJobDetail() {
        return JobBuilder.newJob(WeeklyReportJob.class)
                .withIdentity("WeeklyReportJob")
                .withDescription("Sends weekly report every Monday 9am")
                .storeDurably()
                .build();
    }

    @Bean
    public Trigger weeklyReportTrigger(JobDetail weeklyReportJobDetail) {
        return TriggerBuilder.newTrigger()
                .forJob(weeklyReportJobDetail)
                .withIdentity("WeeklyReportTrigger")
                .withSchedule(CronScheduleBuilder.cronSchedule("0 0 9 ? * MON"))
                .build();
    }
}