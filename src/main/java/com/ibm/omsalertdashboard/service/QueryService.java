package com.ibm.omsalertdashboard.service;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.ibm.omsalertdashboard.model.CoC_IKS;
import com.ibm.omsalertdashboard.model.CoC_Prod;
import com.ibm.omsalertdashboard.model.Master;


@Service
public class QueryService {

	//private String queryKey;
	//private String accountId;
	
	
//	public QueryService(String queryKey, String accountId) {
//		super();
//		this.queryKey = queryKey;
//		this.accountId = accountId;
//	}
	
	public String query(Long timestamp,String accountId,String queryKey) throws IOException {
		String jsonBody = null;
		//String accountId = "2136944";
		//String queryKey = "NRIQ-oy3mO0NWd5ERzYGH2bO7S5CIfIbZUo6-";
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
	
	public JsonArray getEvents(String jsonBody) {
		JsonParser parser = new JsonParser();
		JsonArray array = parser.parse(jsonBody).getAsJsonObject().get("results").getAsJsonArray().get(0).getAsJsonObject().get("events").getAsJsonArray();
		return array;
	}
	
	public Long readNewTimestamp(String jsonBody) {
		JsonParser parser = new JsonParser();
		JsonElement timestamp = parser.parse(jsonBody).getAsJsonObject().get("results").getAsJsonArray().get(0)
				.getAsJsonObject().get("events").getAsJsonArray().get(0).getAsJsonObject().get("timestamp"); 
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
}
