package com.ibm.omsalertdashboard.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;


import com.ibm.omsalertdashboard.model.Incidents;


@Repository
public interface IncidentsRepository extends MongoRepository<Incidents,String>{

	
	
	public List<Incidents> findByName(String name);
	public void updateJsonList(Incidents incidents,String name,boolean flag);
	public void updateJsonList(Incidents incidents,String name);
	public void populate(Incidents incidents, String name);
}
