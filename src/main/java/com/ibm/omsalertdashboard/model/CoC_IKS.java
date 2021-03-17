package com.ibm.omsalertdashboard.model;

import java.util.List;
import java.util.Map;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "coc_iks")
public class CoC_IKS {

	@Id
	private String id;
	private List<Map<String, List<CoC_IKSEvents>>> results;
	private Map<String, Integer> performanceStats;
	private Map<String, Object> metadata;
	
	public List<Map<String, List<CoC_IKSEvents>>> getResults() {
		return results;
	}
	public void setResults(List<Map<String, List<CoC_IKSEvents>>> results) {
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
	
	
}
