package com.example.spring_batch_csv_txt.listner;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;

public class CustomJobListner implements JobExecutionListener {
    @Override
    public void beforeJob(JobExecution jobExecution) {
        System.out.println("Before Job: " + jobExecution.getJobId() + " " + jobExecution.getJobInstance());
    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        System.out.println("After Job: " + jobExecution.getJobId() + " " + jobExecution.getJobInstance());
    }
}
