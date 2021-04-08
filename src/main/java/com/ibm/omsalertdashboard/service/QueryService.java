package com.ibm.omsalertdashboard.service;

import java.io.IOException;
import java.net.URLEncoder;
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

	//private String queryKey;
	//private String accountId;
	
	
//	public QueryService(String queryKey, String accountId) {
//		super();
//		this.queryKey = queryKey;
//		this.accountId = accountId;
//	}
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
	
	public Master jsonToObjectMaster(String jsonBody) {
		ObjectMapper objectMapper = new ObjectMapper();
		Master master = null;
		try {
			master = objectMapper.readValue(jsonBody, Master.class);
			//System.out.println(master.getResultMap().get(0)+"---------");
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return master;
	}
	
	public CoC_IKS jsonToObjectCoc_IKS(String jsonBody) {
		ObjectMapper objectMapper = new ObjectMapper();
		CoC_IKS coc = null;
		try {
			coc = objectMapper.readValue(jsonBody, CoC_IKS.class);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return coc;
	}
	
	public CoC_Prod jsonToObjectCoc_Prod(String jsonBody) {
		ObjectMapper objectMapper = new ObjectMapper();
		CoC_Prod coc = null;
		try {
			coc = objectMapper.readValue(jsonBody, CoC_Prod.class);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return coc;
	}
	
	public void updateJson(String json, String name) {
//		List<Incidents> list = incidentRepo.findByName(name);/* .get(0).getIncidents().getResults().get(0).get("events") */;
//		if(list == null) return; 
		int i = 0;
		if(name.equalsIgnoreCase("master")) i = 0;
		else if(name.equalsIgnoreCase("coc_prod")) i = 1;
		else i = 2;
		
		Incidents incidents = jsonToObject(json);
		System.out.println("Incidents---"+incidents.toString()); 
		//incidentRepo.updateJsonList(incidents, name); 
		
		//get list stored in db
//		List<Events> oldList = incidentRepo.findByName(name).get(i).getIncidents().getResults().get(0).get("events");
//		//if list is null insert incidents in db and return
//		if(oldList == null) {
//			incidentRepo.updateJsonList(incidents, name); 
//			return;
//		}
//		//get new list from incidents
//		List<Events> newList = incidents.getIncidents().getResults().get(0).get("events");
//		//iterate new list
//		for(int index = 0;index < newList.size();index++) {
//			if(newList.get(index).getCurrent_state().equalsIgnoreCase("open")) {
//				oldList.add(newList.get(index));
//				System.out.println("NEW EVENT ADDED---"+ newList.get(index).getCondition_id());  
//			}
//			else {
//				for(int old=0;old<oldList.size();old++) {
//					if(oldList.get(old).getCondition_id() != newList.get(index).getCondition_id()) continue;
//					oldList.get(old).setCurrent_state(newList.get(index).getCurrent_state()); 
//					System.out.println("OLD EVENT UPDATED---" + oldList.get(old).getCondition_id());
//				}
//			}
//		}
//		
//		incidents.getIncidents().getResults().get(0).put("events", oldList);
//		incidentRepo.updateJsonList(incidents, name); 
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
	
		if(incidentList.get(0).getResults() == null) {
			LOG.info("No results!!!");  
			incidentRepo.updateJsonList(incidents, name);
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
					LOG.info("OLD EVENT UPDATED---"+oldEventsList.get(j).getIncident_id()); 
				}
			}
		}
		
		incidents.getResults().get(0).put("events", oldEventsList);
		incidentRepo.updateJsonList(incidents, name);
		
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
}
