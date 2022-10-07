package com.portfolio.gascharge.batch.quartz;

import org.quartz.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class QuartzConfiguration {

    @Bean
    public JobDetail quartzJobDetail() {
        return JobBuilder.newJob(BatchScheduledJob.class)
                .storeDurably()
                .build();
    }

    @Bean
    public Trigger jobTrigger() {
        SimpleScheduleBuilder simpleScheduleBuilder = SimpleScheduleBuilder.simpleSchedule()
                .withIntervalInMinutes(1).repeatForever();

        return TriggerBuilder.newTrigger()
                .forJob(quartzJobDetail())
                .withSchedule(simpleScheduleBuilder)
                .build();
    }
}
