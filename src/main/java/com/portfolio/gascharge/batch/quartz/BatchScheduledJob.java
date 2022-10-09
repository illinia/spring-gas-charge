package com.portfolio.gascharge.batch.quartz;

import lombok.RequiredArgsConstructor;
import org.quartz.JobExecutionContext;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.util.Date;

@RequiredArgsConstructor
public class BatchScheduledJob extends QuartzJobBean {

    private final Job job;
    private final JobExplorer jobExplorer;
    private final JobLauncher jobLauncher;

    private final ChargeInfoJobConfig chargeInfoJobConfig;

    @Override
    protected void executeInternal(JobExecutionContext context) {

        JobParameters jobParameters = new JobParametersBuilder(jobExplorer)
                .getNextJobParameters(chargeInfoJobConfig.job())
                .addDate("date", new Date())
                .toJobParameters();

        try {
            System.out.println("BatchScheduledJob 실행");
            jobLauncher.run(chargeInfoJobConfig.job(), jobParameters);
        } catch (Exception e) {
            System.out.println("executeInternal error");
            e.printStackTrace();
        }
    }
}
