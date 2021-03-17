package com.ibm.omsalertdashboard.timerinfo;

public class TimerInfo {

	private int totalFireCount;//how many times our timer is going to fire
	private boolean runForever;//if this true then timer is going to run forever,then above attribute does not matter
	private long repeatIntervalMs;//in millisec, after how much time timer is going to fire
	private long initialOffsetMs;//after how long first timer fires
	private String callbackData;// something we want to pass to our job
	
	public int getTotalFireCount() {
		return totalFireCount;
	}
	public void setTotalFireCount(int totalFireCount) {
		this.totalFireCount = totalFireCount;
	}
	public boolean isRunForever() {
		return runForever;
	}
	public void setRunForever(boolean runForever) {
		this.runForever = runForever;
	}
	public long getRepeatIntervalMs() {
		return repeatIntervalMs;
	}
	public void setRepeatIntervalMs(long repeatIntervalMs) {
		this.repeatIntervalMs = repeatIntervalMs;
	}
	public long getInitialOffsetMs() {
		return initialOffsetMs;
	}
	public void setInitialOffsetMs(long initialOffsetMs) {
		this.initialOffsetMs = initialOffsetMs;
	}
	public String getCallbackData() {
		return callbackData;
	}
	public void setCallbackData(String callbackData) {
		this.callbackData = callbackData;
	}
	
	
}
