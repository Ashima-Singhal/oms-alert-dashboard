package com.ibm.omsalertdashboard.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.ibm.omsalertdashboard.model.JwtRequest;

@Repository
public interface JwtRequestRepository extends MongoRepository<JwtRequest, String>{

	public JwtRequest findByUsername(String username);
}
