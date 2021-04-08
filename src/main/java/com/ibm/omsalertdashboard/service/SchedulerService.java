package com.ibm.omsalertdashboard.service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ibm.omsalertdashboard.timerinfo.TimerInfo;
import com.ibm.omsalertdashboard.util.TimerUtil;



@Service
public class SchedulerService {

	@Autowired
	private Scheduler scheduler;
	private static final Logger LOG = LoggerFactory.getLogger(SchedulerService.class);
	
//	@Autowired
//	public SchedulerService(Scheduler scheduler) {
//		super();
//		this.scheduler = scheduler;
//	}
	
	//method to schedule job
	public void schedule(final Class jobClass, final TimerInfo info) {
		final JobDetail jobDetail = TimerUtil.buildJobDetail(jobClass, info);
		final Trigger trigger = TimerUtil.buildTrigger(jobClass, info);
		
		try {
			scheduler.scheduleJob(jobDetail, trigger);
		} catch (SchedulerException e) {
			// TODO Auto-generated catch block
			LOG.error(e.getMessage(),e); 
		}
	}
	
	@PostConstruct
	public void init() {
		try {
			scheduler.start();
		} catch (SchedulerException e) {
			// TODO Auto-generated catch block
			LOG.error(e.getMessage(),e); 
		}
	}
	
	@PreDestroy
	public void preDestroy() {
		try {
			scheduler.shutdown();
		} catch (SchedulerException e) {
			// TODO Auto-generated catch block
			LOG.error(e.getMessage(),e); 
		}
	}
}
