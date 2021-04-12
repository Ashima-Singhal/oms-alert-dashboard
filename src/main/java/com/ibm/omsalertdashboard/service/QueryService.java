package com.ibm.omsalertdashboard.service;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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
	
	
	public void update(Incidents incidents,String name) {
		List<Incidents> incidentList = incidentRepo.findByName(name);
	
		if(incidentList == null) {
			LOG.info("No results!!!");  
			incidentRepo.updateJsonList(incidents, name, false); //false means there were no data before
			return;
		}
		 
		List<Events> newEventsList = incidents.getResults().get(0).get("events");
		List<Events> oldEventsList = incidentList.get(0).getResults().get(0).get("events");
		
		for(int i=0;i<newEventsList.size();i++) {
			if(newEventsList.get(i).getCurrent_state().equalsIgnoreCase("open")) {
				oldEventsList.add(newEventsList.get(i));
				LOG.info("NEW EVENT ADDED---"+newEventsList.get(i).getIncident_id());
				continue;
			}
			else {
				for(int j=0;j<oldEventsList.size();j++) {
					if(oldEventsList.get(j).getIncident_id() != newEventsList.get(i).getIncident_id()) continue;
					oldEventsList.get(j).setCurrent_state(newEventsList.get(i).getCurrent_state());
					//add stmt to update end time
					oldEventsList.get(j).setEndTimestamp(newEventsList.get(i).getTimestamp()); 
					LOG.info("OLD EVENT UPDATED---"+oldEventsList.get(j).getIncident_id()+" START TIME= "+oldEventsList.get(j).getTimestamp()+" END TIME= "+oldEventsList.get(j).getEndTimestamp()); 
				}
			}
		}
		
		incidents.getResults().get(0).put("events", oldEventsList);
		incidentRepo.updateJsonList(incidents, name, true); //true means there were data before
		
		LOG.info("Json data successfully inserted for "+name+" account!!!");  
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
}
