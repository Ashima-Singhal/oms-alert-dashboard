package com.ibm.omsalertdashboard.model;

import java.util.List;
import java.util.Map;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "incidents")
public class Incidents {

	@Id
	private String id;
	private String name;
	private List<Map<String, List<Events>>> result;
	private Map<String, Integer> performanceStats;
	private Map<String, Object> metadata;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<Map<String, List<Events>>> getResults() {
		return result;
	}
	public void setResults(List<Map<String, List<Events>>> result) {
		this.result = result;
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
		return "Incidents [id=" + id + ", name=" + name + ", result=" + result + ", performanceStats="
				+ performanceStats + ", metadata=" + metadata + "]";
	}
	
	
	
	
}
