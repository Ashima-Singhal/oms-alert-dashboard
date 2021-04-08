package com.ibm.omsalertdashboard.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.ibm.omsalertdashboard.model.CoC_IKS;
import com.ibm.omsalertdashboard.model.CoC_Prod;
import com.ibm.omsalertdashboard.model.Incidents;
import com.ibm.omsalertdashboard.model.Master;

@Repository
public interface IncidentsRepository extends MongoRepository<Incidents,String>{

	public void updateMasterIncidents(Master master,String name);
	public void updateCocProdIncidents(CoC_Prod prod,String name);
	public void updateCocIksIncidents(CoC_IKS iks,String name);
	
	public List<Incidents> findByName(String name);
	public void updateJsonList(Incidents incidents,String name);
}
