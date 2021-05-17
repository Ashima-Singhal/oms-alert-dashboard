package com.ibm.omsalertdashboard;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
//import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import com.ibm.omsalertdashboard.service.QueryService;


@SpringBootApplication
//@EnableAutoConfiguration(exclude={DataSourceAutoConfiguration.class})
//@EnableJpaRepositories
public class OmsAlertDashboardApplication {
	@Autowired
	private static final Logger LOG = LoggerFactory.getLogger(OmsAlertDashboardApplication.class);
	
	public static void main(String[] args) {
		SpringApplication.run(OmsAlertDashboardApplication.class, args);
		LOG.info("HELLO WORLD!!!!!!!!!!!!!!!!!!!!");
	}  

}
