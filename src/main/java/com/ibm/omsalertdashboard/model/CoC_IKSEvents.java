package com.ibm.omsalertdashboard.model;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document
public class CoC_IKSEvents {

	private long account_id;
	private String account_name;
	private String actionOwner;
	private String alertType;
	private int closed_violations_count_critical;
	private int closed_violations_count_warning;
	private long condition_family_id;
	private long condition_id;
	private String condition_name;
	private String current_state;
	private String details;
	private long duration;
	private String event_type;
	private String incident_acknowledge_url;
	private long incident_id;
	private String incident_url;
	private String namespaceName;
	private int open_violations_count_critical;
	private int open_violations_count_warning;
	private String owner;
	private String policy_name;
	private String policy_url;
	private String runbook_url;
	private String sev;
	public String getSev() {
		return sev;
	}
	public void setSev(String sev) {
		this.sev = sev;
	}
	private String severity;
	private long timestamp;
	private String violation_callback_url;
	private String violation_chart_url;
	
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
	public String getActionOwner() {
		return actionOwner;
	}
	public void setActionOwner(String actionOwner) {
		this.actionOwner = actionOwner;
	}
	public String getAlertType() {
		return alertType;
	}
	public void setAlertType(String alertType) {
		this.alertType = alertType;
	}
	public int getClosed_violations_count_critical() {
		return closed_violations_count_critical;
	}
	public void setClosed_violations_count_critical(int closed_violations_count_critical) {
		this.closed_violations_count_critical = closed_violations_count_critical;
	}
	public int getClosed_violations_count_warning() {
		return closed_violations_count_warning;
	}
	public void setClosed_violations_count_warning(int closed_violations_count_warning) {
		this.closed_violations_count_warning = closed_violations_count_warning;
	}
	public long getCondition_family_id() {
		return condition_family_id;
	}
	public void setCondition_family_id(long condition_family_id) {
		this.condition_family_id = condition_family_id;
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
	public long getDuration() {
		return duration;
	}
	public void setDuration(long duration) {
		this.duration = duration;
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
	public String getNamespaceName() {
		return namespaceName;
	}
	public void setNamespaceName(String namespaceName) {
		this.namespaceName = namespaceName;
	}
	public int getOpen_violations_count_critical() {
		return open_violations_count_critical;
	}
	public void setOpen_violations_count_critical(int open_violations_count_critical) {
		this.open_violations_count_critical = open_violations_count_critical;
	}
	public int getOpen_violations_count_warning() {
		return open_violations_count_warning;
	}
	public void setOpen_violations_count_warning(int open_violations_count_warning) {
		this.open_violations_count_warning = open_violations_count_warning;
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
	public String getViolation_callback_url() {
		return violation_callback_url;
	}
	public void setViolation_callback_url(String violation_callback_url) {
		this.violation_callback_url = violation_callback_url;
	}
	public String getViolation_chart_url() {
		return violation_chart_url;
	}
	public void setViolation_chart_url(String violation_chart_url) {
		this.violation_chart_url = violation_chart_url;
	}
	
	
}
