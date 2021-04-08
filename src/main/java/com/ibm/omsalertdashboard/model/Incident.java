package com.ibm.omsalertdashboard.model;

import java.util.List;
import java.util.Map;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.stereotype.Repository;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Document
@JsonIgnoreProperties(ignoreUnknown = true) 
public class Incident {

	@Id
	private String id;
	private List<Map<String, List<Events>>> results;
	private Map<String, Integer> performanceStats;
	private Map<String, Object> metadata;
	
	public List<Map<String, List<Events>>> getResults() {
		return results;
	}
	public void setResults(List<Map<String, List<Events>>> results) {
		this.results = results;
	}
	public Map<String, Integer> getPerformanceStats() {
		return performanceStats;
	}
	public void setPerformanceStats(Map<String, Integer> performanceStats) {
		this.performanceStats = performanceStats;
	}
	public Map<String, Object> getMetadata() {
		return metadata;
	}
	public void setMetadata(Map<String, Object> metadata) {
		this.metadata = metadata;
	}
	@Override
	public String toString() {
		return "Incident [id=" + id + ", results=" + results + ", performanceStats=" + performanceStats + ", metadata="
				+ metadata + "]";
	}
	
	
}
