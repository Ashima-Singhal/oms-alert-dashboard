package com.ibm.omsalertdashboard.model;

import java.util.List;
import java.util.Map;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonProperty;

@Document(collection = "master")
public class Master {

	@Id
	private String id;
	@JsonProperty("results")
	private List<Map<String, List<MasterEvents>>> results;
	private Map<String, Integer> performanceStats;
	private Map<String, Object> metadata;
	
	public List<Map<String, List<MasterEvents>>> getResultMap() {
		return results;
	}
	public void setResultMap(List<Map<String, List<MasterEvents>>> resultMap) {
		this.results = resultMap;
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
		return "[id=" + id + ", results=" + results + ", performanceStats=" + performanceStats + ", metadata="
				+ metadata + "]";
	}
	
	
}
