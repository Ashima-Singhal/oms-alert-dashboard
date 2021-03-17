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
import com.ibm.omsalertdashboard.model.Key;
import com.ibm.omsalertdashboard.model.Master;
import com.ibm.omsalertdashboard.model.TimestampUtil;
import com.ibm.omsalertdashboard.repository.Coc_IKSRepository;
import com.ibm.omsalertdashboard.repository.Coc_ProdRepository;
import com.ibm.omsalertdashboard.repository.KeyRepository;
import com.ibm.omsalertdashboard.repository.MasterRepository;
import com.ibm.omsalertdashboard.repository.TimestampRepository;
import com.ibm.omsalertdashboard.service.QueryService;

@Component
public class OmsJob implements Job{

	private final QueryService queryService;
	private final KeyRepository keyRepository;
	private final TimestampRepository timestampRepository;
	private final MasterRepository masterRepository;
	private final Coc_IKSRepository cocIksRepository;
	private final Coc_ProdRepository cocProdRepository;
	
	@Autowired
	public OmsJob(final QueryService queryService,final KeyRepository keyRepository,final TimestampRepository timestampRepository,final MasterRepository masterRepository,
				final Coc_IKSRepository cocIksRepository,final Coc_ProdRepository cocProdRepository) {
		super();
		this.queryService = queryService;
		this.keyRepository = keyRepository;
		this.timestampRepository = timestampRepository;
		this.masterRepository = masterRepository;
		this.cocIksRepository = cocIksRepository;
		this.cocProdRepository = cocProdRepository;
	}


	private static final Logger LOG = LoggerFactory.getLogger(OmsJob.class);
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		// TODO Auto-generated method stub
		LOG.info("Executing Oms Job!!!"); 
		insertMaster();
		insertCocIks();
		insertCocProd();
	}

	
	public void insertMaster() {
		try {
			Key key = keyRepository.findOneByName("master");
			//System.out.println("--------"+key.getAccount_id());
			TimestampUtil oldTimestamp = timestampRepository.findOneByName("master");
			System.out.println("old time-"+oldTimestamp.getTimestamp()); 
			String json = queryService.query(oldTimestamp.getTimestamp(), key.getAccount_id(), key.getQuery_key()); 
			System.out.println("JSON response-"+ json); 
			//System.out.println(json);
			
			if(queryService.getEvents(json).size() == 0) {
				LOG.info("No new events!!!");
				return;
			}
			
			Long newTimestamp = queryService.readNewTimestamp(json);
			System.out.println("new time---"+newTimestamp);
			timestampRepository.updateTimestamp("master", newTimestamp);
			LOG.info("timestamp successfully updated!!!"); 
			Master master = queryService.jsonToObjectMaster(json);
			masterRepository.save(master);
			LOG.info("successfully inserted in master collection!!!"); 
		} catch (IOException e) {
			// TODO Auto-generated catch block
			LOG.error(e.getMessage(), e); 
		}
	}
	
	public void insertCocIks() {
		Key key = keyRepository.findOneByName("coc_iks");
		TimestampUtil timestamp = timestampRepository.findOneByName("coc_iks");
		System.out.println("Old time-"+timestamp.getTimestamp()); 
		try {
			String json = queryService.query(timestamp.getTimestamp(), key.getAccount_id(), key.getQuery_key());
			if(queryService.getEvents(json).size() == 0) {
				LOG.info("No new events!!!");
				return;
			}
			
			Long newTimestamp = queryService.readNewTimestamp(json);
			System.out.println("new time-"+newTimestamp); 
			timestampRepository.updateTimestamp("coc_iks", newTimestamp);
			LOG.info("Timestamp successfully updated!!!");
			CoC_IKS coc = queryService.jsonToObjectCoc_IKS(json);
			cocIksRepository.save(coc);
			LOG.info("successfully inserted in coc_iks collection!!!");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void insertCocProd() {
		Key key = keyRepository.findOneByName("coc_prod");
		TimestampUtil timestamp = timestampRepository.findOneByName("coc_prod");
		System.out.println("old time-"+timestamp.getTimestamp());
		try {
			String json = queryService.query(timestamp.getTimestamp(), key.getAccount_id(), key.getQuery_key());
			if(queryService.getEvents(json).size() == 0) {
				LOG.info("No new events!!!");
				return;
			}
			
			Long newTimestamp = queryService.readNewTimestamp(json);
			System.out.println("New time-"+newTimestamp);
			timestampRepository.updateTimestamp("coc_prod", newTimestamp);
			LOG.info("Timestamp of Coc_Prod successfully updated!!!");
			CoC_Prod coc = queryService.jsonToObjectCoc_Prod(json);
			cocProdRepository.save(coc);
			LOG.info("successfully inserted in coc_prod collection!!!"); 
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
