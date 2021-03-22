package com.ibm.omsalertdashboard.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
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
import com.ibm.omsalertdashboard.model.Master;
import com.ibm.omsalertdashboard.model.MasterEvents;
import com.ibm.omsalertdashboard.repository.MasterRepository;
import com.ibm.omsalertdashboard.service.OmsService;

@RestController
@CrossOrigin
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
	public ResponseEntity<List<MasterEvents>> getMasterEvents(@RequestParam("status") String status) throws JsonProcessingException {
//		List masterEvents = new ArrayList();
//		System.out.println(masterRepository.findAll().get(0)); 
		List<Master> master = omsService.getLatestRecordMaster();
		
		//Long test = master.get(0).getResultMap().get(0).get("events").get(0).getAccount_id();
		//System.out.println("Test----"+test); 
		
		//System.out.println(json); 
		//return ResponseEntity.ok(test);
		//return ResponseEntity.ok(master.getResultMap()); 
		
		List<MasterEvents> result = new ArrayList<>();
		for(int i=0;i<master.size();i++) {
			List<MasterEvents> list = master.get(i).getResultMap().get(0).get("events"); 
			for(int j=0;j<list.size();j++) {
				String cur_status = list.get(j).getCurrent_state();
				if(cur_status.equals(status)) result.add(list.get(j)); 
			}
		}
		return ResponseEntity.ok(result);
	}
	
	@GetMapping("/prod") 
	public ResponseEntity<List<CoC_ProdEvents>> getProdEvents(@RequestParam("status") String status){
		List<CoC_Prod> coc = omsService.getLatestRecordProd();
		//return ResponseEntity.ok(coc.getResults());
		
		List<CoC_ProdEvents> result = new ArrayList<>();
		for(int i=0;i<coc.size();i++) {
			List<CoC_ProdEvents> list = coc.get(i).getResults().get(0).get("events");
			for(int j=0;j<list.size();j++) {
				String cur_status = list.get(j).getCurrent_state();
				if(cur_status.equalsIgnoreCase(status)) result.add(list.get(j));
			}
		}
		return ResponseEntity.ok(result);
	}
	
	@GetMapping("/iks")
	public ResponseEntity<List<CoC_IKSEvents>> getIksEvents(@RequestParam("status") String status){
//		CoC_IKS coc = omsService.getLatestRecordIks();
//		return ResponseEntity.ok(coc.getResults());
		
		List<CoC_IKS> coc = omsService.getLatestRecordIks();
		List<CoC_IKSEvents> result = new ArrayList<>();
		
		for(int i=0;i<coc.size();i++) {
			List<CoC_IKSEvents> list = coc.get(i).getResults().get(0).get("events");
			for(int j=0;j<list.size();j++) {
				String cur_status = list.get(j).getCurrent_state();
				if(cur_status.equalsIgnoreCase(status)) result.add(list.get(j));
			}
		}
		return ResponseEntity.ok(result);
	}
}
