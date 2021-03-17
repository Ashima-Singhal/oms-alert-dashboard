package com.ibm.omsalertdashboard.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.ibm.omsalertdashboard.model.Key;

@Repository
public interface KeyRepository extends MongoRepository<Key, String>{

	public Key findOneByName(String name);
}
