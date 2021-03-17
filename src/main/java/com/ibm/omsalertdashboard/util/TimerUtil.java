package com.ibm.omsalertdashboard.util;

import java.util.Date;

import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;

import com.ibm.omsalertdashboard.timerinfo.TimerInfo;

// stores the details how to build timer
public class TimerUtil {
	
	private TimerUtil() {}
	
	//method going to build job details
	//jobclass - which class we want to store details for
	//info - information of the timer
	public static JobDetail buildJobDetail(final Class jobClass,final TimerInfo info) {
		final JobDataMap jobDataMap = new JobDataMap();//map where we can keep all data of our job - stored together with timer
		jobDataMap.put(jobClass.getSimpleName(), info);
		
		return JobBuilder
				.newJob(jobClass)
				.withIdentity(jobClass.getSimpleName())
				.setJobData(jobDataMap)
				.build();
	}
	
	
	public static Trigger buildTrigger(final Class jobClass, final TimerInfo info) {
		SimpleScheduleBuilder builder = SimpleScheduleBuilder.simpleSchedule().withIntervalInMilliseconds(info.getRepeatIntervalMs());
		
		if(info.isRunForever()) {
			builder = builder.repeatForever();
		}else {
			builder = builder.withRepeatCount(info.getTotalFireCount() - 1);
		}
		
		return TriggerBuilder
				.newTrigger()
				.withIdentity(jobClass.getSimpleName())
				.withSchedule(builder)
				.startAt(new Date(System.currentTimeMillis() + info.getInitialOffsetMs()))
				.build();
	}
}
