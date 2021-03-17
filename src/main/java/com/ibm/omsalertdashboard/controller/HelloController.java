package com.ibm.omsalertdashboard.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ibm.omsalertdashboard.application.OmsApplication;

@RestController
public class HelloController {

	private OmsApplication service;
	
	@Autowired
	public HelloController(OmsApplication service) {
		super();
		this.service = service;
	}

	@RequestMapping("/hello")
	public String hello() {
		return "Hello World!!!";
	}
	
	@PostMapping("/runHelloWorld")
	//trigger for running hello world job
	public void runHelloWorldJob() {
		service.runHelloWorldJob();
	}
}
