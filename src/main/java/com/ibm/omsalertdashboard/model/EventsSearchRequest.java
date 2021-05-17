package com.ibm.omsalertdashboard.model;

public class EventsSearchRequest {

	private String current_state;
	private String account_name;
	private String condition_name;
	private Long timestamp;
	private Long endTimestamp;
	
	public EventsSearchRequest(String current_state, String account_name, String condition_name, Long timestamp,
			Long endTimestamp) {
		super();
		this.current_state = current_state;
		this.account_name = account_name;
		this.condition_name = condition_name;
		this.timestamp = timestamp;
		this.endTimestamp = endTimestamp;
	}

	public String getCurrent_state() {
		return current_state;
	}

	public void setCurrent_state(String current_state) {
		this.current_state = current_state;
	}

	public String getAccount_name() {
		return account_name;
	}

	public void setAccount_name(String account_name) {
		this.account_name = account_name;
	}

	public String getCondition_name() {
		return condition_name;
	}

	public void setCondition_name(String condition_name) {
		this.condition_name = condition_name;
	}

	public Long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Long timestamp) {
		this.timestamp = timestamp;
	}

	public Long getEndTimestamp() {
		return endTimestamp;
	}

	public void setEndTimestamp(Long endTimestamp) {
		this.endTimestamp = endTimestamp;
	}

	@Override
	public String toString() {
		return "EventsSearchRequest [current_state=" + current_state + ", account_name=" + account_name
				+ ", condition_name=" + condition_name + ", timestamp=" + timestamp + ", endTimestamp=" + endTimestamp
				+ "]";
	}
	
	
}
