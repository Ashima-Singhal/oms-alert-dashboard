package com.ibm.omsalertdashboard.model;

import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class CoC_ProdEvents {

	private long account_id;
	private String account_name;
	private long condition_id;
	private String condition_name;
	private String current_state;
	private String details;
	private String event_type;
	private String incident_acknowledge_url;
	private long incident_id;
	private String incident_url;
	private String owner;
	private String policy_name;
	private String policy_url;
	private String runbook_url;
	private String severity;
	private long timestamp;
	
	public long getAccount_id() {
		return account_id;
	}
	public void setAccount_id(long account_id) {
		this.account_id = account_id;
	}
	public String getAccount_name() {
		return account_name;
	}
	public void setAccount_name(String account_name) {
		this.account_name = account_name;
	}
	public long getCondition_id() {
		return condition_id;
	}
	public void setCondition_id(long condition_id) {
		this.condition_id = condition_id;
	}
	public String getCondition_name() {
		return condition_name;
	}
	public void setCondition_name(String condition_name) {
		this.condition_name = condition_name;
	}
	public String getCurrent_state() {
		return current_state;
	}
	public void setCurrent_state(String current_state) {
		this.current_state = current_state;
	}
	public String getDetails() {
		return details;
	}
	public void setDetails(String details) {
		this.details = details;
	}
	public String getEvent_type() {
		return event_type;
	}
	public void setEvent_type(String event_type) {
		this.event_type = event_type;
	}
	public String getIncident_acknowledge_url() {
		return incident_acknowledge_url;
	}
	public void setIncident_acknowledge_url(String incident_acknowledge_url) {
		this.incident_acknowledge_url = incident_acknowledge_url;
	}
	public long getIncident_id() {
		return incident_id;
	}
	public void setIncident_id(long incident_id) {
		this.incident_id = incident_id;
	}
	public String getIncident_url() {
		return incident_url;
	}
	public void setIncident_url(String incident_url) {
		this.incident_url = incident_url;
	}
	public String getOwner() {
		return owner;
	}
	public void setOwner(String owner) {
		this.owner = owner;
	}
	public String getPolicy_name() {
		return policy_name;
	}
	public void setPolicy_name(String policy_name) {
		this.policy_name = policy_name;
	}
	public String getPolicy_url() {
		return policy_url;
	}
	public void setPolicy_url(String policy_url) {
		this.policy_url = policy_url;
	}
	public String getRunbook_url() {
		return runbook_url;
	}
	public void setRunbook_url(String runbook_url) {
		this.runbook_url = runbook_url;
	}
	public String getSeverity() {
		return severity;
	}
	public void setSeverity(String severity) {
		this.severity = severity;
	}
	public long getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}
	
	
}
