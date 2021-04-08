package com.ibm.omsalertdashboard.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ibm.omsalertdashboard.application.OmsApplication;
import com.ibm.omsalertdashboard.model.CoC_IKS;
import com.ibm.omsalertdashboard.model.CoC_IKSEvents;
import com.ibm.omsalertdashboard.model.CoC_Prod;
import com.ibm.omsalertdashboard.model.CoC_ProdEvents;
import com.ibm.omsalertdashboard.model.Events;
import com.ibm.omsalertdashboard.model.Incidents;
import com.ibm.omsalertdashboard.model.Master;
import com.ibm.omsalertdashboard.model.MasterEvents;
import com.ibm.omsalertdashboard.repository.MasterRepository;
import com.ibm.omsalertdashboard.service.OmsService;
import com.ibm.omsalertdashboard.service.QueryService;

@RestController
@CrossOrigin
public class OmsController {

	private final OmsApplication service;
	private final MasterRepository masterRepository;
	private final OmsService omsService;
	@Autowired
	QueryService queryService;
	
	@Autowired
	public OmsController(final OmsApplication service,final MasterRepository masterRepository,final OmsService omsService) {
		super();
		this.service = service;
		this.masterRepository = masterRepository;
		this.omsService = omsService;
	}
	
	@GetMapping("/oms")
	public void runOmsJob() {
		service.runOmsJob();
	}
	
	@GetMapping("/events-list") 
	public ResponseEntity<List<Events>> getEvents(@RequestParam("status") String status){
		//queryService.getEvents(status); 
		return ResponseEntity.ok(queryService.getEvents(status));
	}
	
	@PatchMapping("/update-event")
	public ResponseEntity<Events> updateEvent(@RequestParam("alert_id") long alert_id,@RequestParam("slack_url")String slack_url){
		return ResponseEntity.ok(queryService.updateEvent(alert_id, slack_url)); 
	}
}
