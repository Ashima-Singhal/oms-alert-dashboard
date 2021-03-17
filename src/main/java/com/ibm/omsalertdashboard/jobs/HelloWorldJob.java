package com.ibm.omsalertdashboard.jobs;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class HelloWorldJob implements Job{

	private static final Logger LOG = LoggerFactory.getLogger(HelloWorldJob.class);
	
	@Override
	//method called everytime a timer fires
	//code to be executed put here
	public void execute(JobExecutionContext context) throws JobExecutionException {
		LOG.info("Hello World!!!");
	}

}
