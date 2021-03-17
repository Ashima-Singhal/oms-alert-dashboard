package com.ibm.omsalertdashboard.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ibm.omsalertdashboard.application.OmsApplication;
import com.ibm.omsalertdashboard.model.CoC_IKS;
import com.ibm.omsalertdashboard.model.CoC_IKSEvents;
import com.ibm.omsalertdashboard.model.CoC_Prod;
import com.ibm.omsalertdashboard.model.CoC_ProdEvents;
import com.ibm.omsalertdashboard.model.Master;
import com.ibm.omsalertdashboard.model.MasterEvents;
import com.ibm.omsalertdashboard.repository.MasterRepository;
import com.ibm.omsalertdashboard.service.OmsService;

@RestController
public class OmsController {

	private final OmsApplication service;
	private final MasterRepository masterRepository;
	private final OmsService omsService;
	
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
	
	@GetMapping("/master")
	public ResponseEntity<List<Map<String, List<MasterEvents>>>> getMasterEvents() {
//		List masterEvents = new ArrayList();
//		System.out.println(masterRepository.findAll().get(0)); 
		Master master = omsService.getLatestRecordMaster();
		return ResponseEntity.ok(master.getResultMap());  
	}
	
	@GetMapping("/prod") 
	public ResponseEntity<List<Map<String, List<CoC_ProdEvents>>>> getProdEvents(){
		CoC_Prod coc = omsService.getLatestRecordProd();
		return ResponseEntity.ok(coc.getResults());
	}
	
	@GetMapping("/iks")
	public ResponseEntity<List<Map<String, List<CoC_IKSEvents>>>> getIksEvents(){
		CoC_IKS coc = omsService.getLatestRecordIks();
		return ResponseEntity.ok(coc.getResults());
	}
}
