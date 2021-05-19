package com.ibm.omsalertdashboard.jobs;

import java.io.IOException;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ibm.omsalertdashboard.model.CoC_IKS;
import com.ibm.omsalertdashboard.model.CoC_Prod;
import com.ibm.omsalertdashboard.model.Incidents;
import com.ibm.omsalertdashboard.model.Key;
import com.ibm.omsalertdashboard.model.Master;
import com.ibm.omsalertdashboard.model.TimestampUtil;
import com.ibm.omsalertdashboard.repository.Coc_IKSRepository;
import com.ibm.omsalertdashboard.repository.Coc_ProdRepository;
import com.ibm.omsalertdashboard.repository.IncidentsRepository;
import com.ibm.omsalertdashboard.repository.KeyRepository;
import com.ibm.omsalertdashboard.repository.MasterRepository;
import com.ibm.omsalertdashboard.repository.TimestampRepository;
import com.ibm.omsalertdashboard.repositoryImpl.IncidentsRepositoryImpl;
import com.ibm.omsalertdashboard.service.QueryService;

@Component
public class OmsJob implements Job{

	@Autowired
	private  QueryService queryService;
	@Autowired
	private static final Logger LOG = LoggerFactory.getLogger(OmsJob.class);
	@Autowired
	private IncidentsRepositoryImpl incidentRepo;
	
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		
		LOG.info("Executing Oms Job!!!"); 
		insertMaster();
		insertCocIks();
		insertCocProd();
	}

	
	public void insertMaster() {
		try {
			Key key = queryService.findOneByName("master");
			TimestampUtil oldTimestamp = queryService.findTimeByName("master"); 
			String json = queryService.query(oldTimestamp.getTimestamp(), key.getAccount_id(), key.getQuery_key()); 
			
			if(!queryService.getEvents(json, "master")) return;
			
			Long newTimestamp = queryService.readNewTimestamp(json,"master");
			
			queryService.updateTimestamp("master", newTimestamp); 
			
			Incidents incidents = queryService.jsonToObject(json);
			
			queryService.update(incidents, "master");
			//incidentRepo.updateJsonList(incidents, "master",false); 
		} catch (IOException e) {
			
			LOG.error(e.getMessage(), e); 
		}
	}
	
	public void insertCocIks() {
		Key key = queryService.findOneByName("coc_iks");
		TimestampUtil timestamp = queryService.findTimeByName("coc_iks");
		try {
			String json = queryService.query(timestamp.getTimestamp(), key.getAccount_id(), key.getQuery_key());
			
			if(!queryService.getEvents(json, "coc_iks")) return;
			
			Long newTimestamp = queryService.readNewTimestamp(json, "coc_iks");

			queryService.updateTimestamp("coc_iks", newTimestamp); 
			
			Incidents incidents = queryService.jsonToObject(json);
			
			queryService.update(incidents, "coc_iks");  
			//incidentRepo.updateJsonList(incidents, "coc_iks",false); 
		} catch (IOException e) {
			
			LOG.error(e.getMessage(), e);
		}
	}
	
	public void insertCocProd() {
		Key key = queryService.findOneByName("coc_prod");
		TimestampUtil timestamp = queryService.findTimeByName("coc_prod");
		try {
			String json = queryService.query(timestamp.getTimestamp(), key.getAccount_id(), key.getQuery_key());
			
			if(!queryService.getEvents(json, "coc_prod")) return;
			
			Long newTimestamp = queryService.readNewTimestamp(json,"coc_prod");
			
			queryService.updateTimestamp("coc_prod", newTimestamp); 
			Incidents incidents = queryService.jsonToObject(json);
			
			//queryService.update(incidents, "coc_prod");   
			incidentRepo.updateJsonList(incidents, "coc_prod",false);  
		} catch (IOException e) {
		
			LOG.error(e.getMessage(), e);
		}
	}
}
