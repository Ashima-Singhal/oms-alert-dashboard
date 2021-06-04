package com.ibm.omsalertdashboard.jobs;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ibm.omsalertdashboard.model.CoC_IKS;
import com.ibm.omsalertdashboard.model.CoC_Prod;
import com.ibm.omsalertdashboard.model.Events;
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
	private static LocalDateTime currentDate;
	
	


	public LocalDateTime getCurrentDate() {
		return currentDate;
	}



	public void setCurrentDate(LocalDateTime currentDate) {
		this.currentDate = currentDate; 
	}



	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		
		LOG.info("Executing Oms Job!!!"); 
		setCurrentDate(LocalDateTime.now()); 
		insertMaster();
		insertCocIks();
		insertCocProd();
		sendMail();
	}

	
	
	private void sendMail() {
		List<Events> events = queryService.getEventsList("open");
		if(events == null || events.size() == 0) return;
		LOG.info("Preparing to send mail..."); 
		StringBuilder message = new StringBuilder("There are some open alerts. Please look into this");
		message = message.append("\n");
		for(Events event:events) {
			message = message.append(event.getIncident_url());
		}
		String subject = "Open Alerts!!!";
		String to = "Shivani.Sah@ibm.com, pankaj_singh@in.ibm.com";
		String from = "test.ibm.01062021@gmail.com";
		
		sendMail(message.toString(),subject,to,from);
	}



	//this method is responsible to send e mail
	private void sendMail(String message, String subject, String to, String from) {
		 
		//variable for gmail host
		String host = "smtp.gmail.com";
		
		//get the system properties
		Properties properties = System.getProperties();
		
		//setting important properties
		//set host
		properties.put("mail.smtp.host", host);
		//set port
		properties.put("mail.smtp.port", "465");
		properties.put("mail.smtp.ssl.enable", "true");
		properties.put("mail.smtp.auth", "true");
		
		//get session object
		Session session = Session.getInstance(properties, new Authenticator() {

			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				
				return new PasswordAuthentication("test.ibm.01062021@gmail.com", "test01062021");
			}
			
		});
		session.setDebug(true);
		
		//compose the message
		MimeMessage mimeMsg = new MimeMessage(session);
		
		
		try {
			//set from email id
			mimeMsg.setFrom(from);
			//set recepient email id
			//mimeMsg.setRecipient(Message.RecipientType.TO, new InternetAddress(to));
			mimeMsg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to)); 
			//set subject
			mimeMsg.setSubject(subject);
			//set text
			mimeMsg.setText(message); 
			
			//send message
			Transport.send(mimeMsg); 
			
			LOG.info("Mail sent successfully!!!"); 
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
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
			
			queryService.update(incidents, "coc_prod");   
			//incidentRepo.updateJsonList(incidents, "coc_prod",false);  
		} catch (IOException e) {
		
			LOG.error(e.getMessage(), e);
		}
	}
}
