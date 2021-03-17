package com.ibm.omsalertdashboard.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.ibm.omsalertdashboard.model.TimestampUtil;

@Repository
public interface TimestampRepository extends MongoRepository<TimestampUtil, String>{

	public TimestampUtil findOneByName(String name);
	public void updateTimestamp(String name,Long newTimestamp);
}
