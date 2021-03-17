package com.ibm.omsalertdashboard.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "keys")
public class Key {

	@Id
	private String _id;
	private String name;
	private String query_key;
	private String account_id;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getQuery_key() {
		return query_key;
	}
	public void setQuery_key(String query_key) {
		this.query_key = query_key;
	}
	public String getAccount_id() {
		return account_id;
	}
	public void setAccount_id(String account_id) {
		this.account_id = account_id;
	}
	
	
}
