package com.ibm.omsalertdashboard.service;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.ibm.omsalertdashboard.model.CoC_IKS;
import com.ibm.omsalertdashboard.model.CoC_Prod;
import com.ibm.omsalertdashboard.model.Events;
import com.ibm.omsalertdashboard.model.Incidents;
import com.ibm.omsalertdashboard.model.Key;
import com.ibm.omsalertdashboard.model.Master;
import com.ibm.omsalertdashboard.model.TimestampUtil;
import com.ibm.omsalertdashboard.repository.IncidentsRepository;
import com.ibm.omsalertdashboard.repository.TimestampRepository;
import com.ibm.omsalertdashboard.repositoryImpl.IncidentsRepositoryImpl;
import com.ibm.omsalertdashboard.repositoryImpl.KeyRepositoryImpl;
import com.ibm.omsalertdashboard.repositoryImpl.TimestampRepositoryImpl;



@Service
public class QueryService {

	@Autowired
	private static final Logger LOG = LoggerFactory.getLogger(QueryService.class);
	@Autowired
	IncidentsRepositoryImpl incidentRepo;
	@Autowired
	KeyRepositoryImpl keyRepo;
	@Autowired
	TimestampRepositoryImpl timeRepo;
	
	public String query(Long timestamp,String accountId,String queryKey) throws IOException {
		String jsonBody = null;
		final String query = "SELECT * FROM OmsApplicationAlerts where timestamp > "+timestamp;
		if(queryKey == null) return jsonBody;
		
		final String url = "https://insights-api.newrelic.com/v1/accounts/" + accountId + "/query?nrql="
				+ URLEncoder.encode(query, "UTF-8");

		final OkHttpClient client = new OkHttpClient.Builder().connectTimeout(5, TimeUnit.SECONDS)
				.writeTimeout(5, TimeUnit.SECONDS).readTimeout(10, TimeUnit.SECONDS).build();
		final Request request = new Request.Builder().header("X-Query-Key", queryKey).url(url).get().build();
		final Response response = client.newCall(request).execute();
		
		if (response.isSuccessful()) {
			jsonBody = response.body().string();

		} else {

			throw new IOException("Request failed with code " + response.code() + " body: " + response.body().string());
		}

		return jsonBody;
	}
	
	public boolean getEvents(String jsonBody,String name) {
		JsonParser parser = new JsonParser();
		JsonArray array = parser.parse(jsonBody).getAsJsonObject().get("results").getAsJsonArray().get(0).getAsJsonObject().get("events").getAsJsonArray();
		if(array.size() == 0) {
			LOG.info("No New Events in "+name);
			return false;
		}
		return true;
	}
	
	public Long readNewTimestamp(String jsonBody,String name) {
		JsonParser parser = new JsonParser();
		JsonElement timestamp = parser.parse(jsonBody).getAsJsonObject().get("results").getAsJsonArray().get(0)
				.getAsJsonObject().get("events").getAsJsonArray().get(0).getAsJsonObject().get("timestamp"); 
		LOG.info("New Timestamp for "+name+" ="+ timestamp.getAsString()); 
		return Long.parseLong(timestamp.getAsString());
	}
	
	
	public Incidents jsonToObject(String jsonBody) {
		ObjectMapper objectMapper = new ObjectMapper();
		Incidents incidents = null;
		try {
			incidents = objectMapper.readValue(jsonBody, Incidents.class);
		} catch (JsonProcessingException e) { 
			e.printStackTrace();
		}
		return incidents;
	}
	
	//service method to insert incidents list in db
	//if incident is open insert directly
	//else update the status of already opened status
	public void update(Incidents incidents,String name) { 
				
		List<Incidents> incidentList = incidentRepo.findByName(name);//retrieving incident list from db
		if(incidentList == null) {
			LOG.info("NO OLD INCIDENTS");
			incidentRepo.updateJsonList(incidents, name, false);
			return;
		}
		
		Map<Long, Events> incidentMap = new HashMap<>();
		List<Events> oldEventlList = incidentList.get(0).getResults().get(0).get("events");
		//loop to put all incidents retrieved from db into hashmap with incident id as key
		for(int i=0;i<oldEventlList.size();i++) {
			if(!incidentMap.containsKey(oldEventlList.get(i).getIncident_id())) 
				incidentMap.put(oldEventlList.get(i).getIncident_id(), oldEventlList.get(i));
		}
		
		//putting new events retrieved from new relic api in newEventsList
		List<Events> newEventsList = incidents.getResults().get(0).get("events");
		Set<Long> newIncidentIdSet = new HashSet<>();
		//updating all the events 
		for(int i=0;i<newEventsList.size();i++) {
			if(!newIncidentIdSet.contains(newEventsList.get(i).getIncident_id())) {
				incidentMap.put(newEventsList.get(i).getIncident_id(), newEventsList.get(i));
				LOG.info("i am inside this condition"); 
			}
			
			
			newIncidentIdSet.add(newEventsList.get(i).getIncident_id());
			
		}
		
		//iterate hashmap to put events in a list
		Set<Long> keys = incidentMap.keySet();
		List<Events> upadatedList = new ArrayList<>();
		for(Long key:keys) {
			upadatedList.add(incidentMap.get(key));
		}
		
		incidents.getResults().get(0).put("events", upadatedList);
		incidentRepo.updateJsonList(incidents, name, true);
		LOG.info("JSON DATA SUCCESSFULLY INSERTED FOR "+name+" ACCOUNT");  
	}
	
	public Key findOneByName(String name) {
		return keyRepo.findOneByName(name);
	}
	
	public TimestampUtil findTimeByName(String name) {
		TimestampUtil timestamp = timeRepo.findOneByName(name);
		LOG.info("Old timestamp for "+name+" account = "+ timestamp.getTimestamp());
		return timestamp;
	}
	
	public void updateTimestamp(String name, Long newTimestamp) {
		timeRepo.updateTimestamp(name, newTimestamp); 
		LOG.info("Timestamp for "+name+" successfully updated!!!");  
	}
	
	//method to get events based on current status and customer name
	public List<Events> getEventsList(String status,String account_name){
		 if(account_name.equalsIgnoreCase("")) return getEventsList(status);
		
		 List<Incidents> incidentList = incidentRepo.findAll();
		 List<Events> eventsList = new ArrayList<>();
		 
		 for(int i=0;i<incidentList.size();i++) {
			 List<Events> tempList = incidentList.get(i).getResults().get(0).get("events");
			 for(Events event:tempList) {
				 if(event.getCurrent_state().equalsIgnoreCase(status) && event.getAccount_name().equalsIgnoreCase(account_name)) 
					 eventsList.add(event);
			 }
		 }
		 
		 return eventsList;
	}
	
	public List<Events> getEventsList(String status){
		
		 List<Incidents> incidentList = incidentRepo.findAll();
		 List<Events> eventsList = new ArrayList<>();
		 
		 for(int i=0;i<incidentList.size();i++) {
			 List<Events> tempList = incidentList.get(i).getResults().get(0).get("events");
			 for(Events event:tempList) {
				 if(event.getCurrent_state().equalsIgnoreCase(status)) 
					 eventsList.add(event);
			 }
		 }
		 
		 return eventsList;
	}
	
	public Events updateEvent(long incident_id,String slack_url) {
		List<Incidents> incidentList = incidentRepo.findAll();
		String name = null;
		for(int i=0;i<incidentList.size();i++) {
			List<Events> tempList = incidentList.get(i).getResults().get(0).get("events");
			for(Events event: tempList) {
				if(event.getIncident_id() == incident_id) {
					name = incidentList.get(i).getName();
					break;
				}
			}
		}
		return update(incidentRepo.findByName(name).get(0), incident_id, name, slack_url);
	}
	
	public Events update(Incidents incident,long incident_id,String name,String slack_url) {
		List<Events> events = incident.getResults().get(0).get("events");
		Events e = null;
		for(Events event: events) {
			if(event.getIncident_id() == incident_id) {
				event.setSlack_url(slack_url);
				e = event;
				break;
			}
		}
		
		incident.getResults().get(0).put("events", events);
		
		incidentRepo.updateJsonList(incident, name);
		
		LOG.info("Slack URL for incident id "+incident_id+" successfully updated");  
		return e;
	}
	
	public Object[] findAllCustomers(){
		List<Incidents> incidents = incidentRepo.findAll();
		Set<String> customerSet = new HashSet<>();
		for(int i=0;i<incidents.size();i++) {
			List<Events> eventList = incidents.get(i).getResults().get(0).get("events");
			for(Events event:eventList)
				customerSet.add(event.getAccount_name());
		}
		return customerSet.toArray();
	}
}
