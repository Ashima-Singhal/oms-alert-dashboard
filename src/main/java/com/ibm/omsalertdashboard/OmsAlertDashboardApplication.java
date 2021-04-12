package com.ibm.omsalertdashboard;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.ibm.omsalertdashboard.service.QueryService;


@SpringBootApplication
public class OmsAlertDashboardApplication {
	@Autowired
	private static final Logger LOG = LoggerFactory.getLogger(OmsAlertDashboardApplication.class);
	
	public static void main(String[] args) {
		SpringApplication.run(OmsAlertDashboardApplication.class, args);
		LOG.info("HELLO WORLD!!!!!!!!!!!!!!!!!!!!");
	}  

}
