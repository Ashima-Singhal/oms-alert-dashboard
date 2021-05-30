package com.ibm.omsalertdashboard.controller;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
import com.ibm.omsalertdashboard.model.EventsSearchRequest;
import com.ibm.omsalertdashboard.model.Incidents;
import com.ibm.omsalertdashboard.model.JwtResponse;
import com.ibm.omsalertdashboard.model.Key;
import com.ibm.omsalertdashboard.model.Master;
import com.ibm.omsalertdashboard.model.MasterEvents;
import com.ibm.omsalertdashboard.model.RegisterResponse;
import com.ibm.omsalertdashboard.model.JwtRequest;
import com.ibm.omsalertdashboard.repository.MasterRepository;
import com.ibm.omsalertdashboard.service.CustomUserDetailsService;
import com.ibm.omsalertdashboard.service.OmsService;
import com.ibm.omsalertdashboard.service.QueryService;
import com.ibm.omsalertdashboard.util.JwtUtil;

@RestController
@CrossOrigin
public class OmsController {

	private final OmsApplication service;
	private final MasterRepository masterRepository;
	private final OmsService omsService;
	@Autowired
	private QueryService queryService;
	
	@Autowired
	private CustomUserDetailsService customUserDetailsService;
	
	@Autowired
	private JwtUtil jwtUtil;
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
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
	
	@GetMapping("/populate")
	public void initialSetup() {
		queryService.populateMaster();
		queryService.populateIks();
		queryService.populateProd();
	}
	
	@GetMapping("/events-list") 
	public ResponseEntity<List<Events>> getEvents(@RequestParam("status") String status,@RequestParam("account_name") String account_name){
		//queryService.getEvents(status); 
		if(account_name == null || account_name.length() == 0) return ResponseEntity.ok(queryService.getEventsList(status)); 
		return ResponseEntity.ok(queryService.getEventsList(status, account_name)); 
	}
	
	@PostMapping("/events")
	public ResponseEntity<List<Events>> getEvents(@RequestBody EventsSearchRequest eventSearchReq){
		System.out.println("API hit-"+eventSearchReq.getAccount_name().size());
		List<Events> eventList = queryService.getEventsList(eventSearchReq.getCurrent_state(), eventSearchReq.getAccount_name(), eventSearchReq.getCondition_name(), eventSearchReq.getTimestamp(), eventSearchReq.getEndTimestamp());
		return ResponseEntity.ok(eventList);
	}
	
	@PatchMapping("/update-event")
	public ResponseEntity<Events> updateEvent(@RequestParam("alert_id") long alert_id,@RequestBody Map<String,String> event){
		//System.out.println("API hit---"+ alert_id+" "+event.get("slack_url")); 
		return ResponseEntity.ok(queryService.updateEvent(alert_id, event.get("slack_url")));  
	}
	
	@GetMapping("/get-customers")
	public ResponseEntity<Object[]> getAllCustomers(){
		return ResponseEntity.ok(queryService.findAllCustomers());
	}
	
	@GetMapping("/get-conditions")
	public ResponseEntity<Object[]> getAllConditions(){
		return ResponseEntity.ok(queryService.findAllConditions());
	}
	
	@PostMapping("/token")
	public ResponseEntity<JwtResponse> generateToken(@RequestBody JwtRequest user) throws Exception{
		System.out.println("User data received-"+user); 
		try {
			this.authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUserName(), user.getPassword()));
		}catch (UsernameNotFoundException e) {
			e.printStackTrace();
			throw new Exception("User not found!!!"); 
		}catch(BadCredentialsException e) {
			e.printStackTrace();
			throw new Exception("Bad Credentials!!!"); 
		}
		
		//fine area
		UserDetails userDetails = this.customUserDetailsService.loadUserByUsername(user.getUserName());
		String token = this.jwtUtil.generateToken(userDetails);
		System.out.println("generated token - "+ token); 
		
		return ResponseEntity.ok(new JwtResponse(token,user.getUserName()));
	}
	
	
	@PostMapping("/register")
	public ResponseEntity<RegisterResponse> register(@RequestBody JwtRequest user) {
		JwtRequest response = queryService.save(user);
		if(response == null)
			return ResponseEntity.status(500).body(new RegisterResponse(user.getUserName(), user.getRole(), "User already exits"));  
		return ResponseEntity.ok(new RegisterResponse(user.getUserName(), user.getRole(), "User successfully created")); 
	}
	
	@GetMapping("/getDate")
	public ResponseEntity<LocalDateTime> getDate() { 
		return ResponseEntity.ok(queryService.getDate());
	}
}
