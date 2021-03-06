package com.ibm.omsalertdashboard.application;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ibm.omsalertdashboard.jobs.OmsJob;
import com.ibm.omsalertdashboard.service.SchedulerService;
import com.ibm.omsalertdashboard.timerinfo.TimerInfo;

@Service 
public class OmsApplication {

	@Autowired
	private SchedulerService scheduler;
	
	// method to run oms job created
	public void runOmsJob() {
		final TimerInfo info = new TimerInfo();
		//info.setTotalFireCount(1);
		info.setRunForever(true); 
		info.setRepeatIntervalMs(600000); 
		info.setInitialOffsetMs(1000);
		
		scheduler.schedule(OmsJob.class, info); 
	}
}
