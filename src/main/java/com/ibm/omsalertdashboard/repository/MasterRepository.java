package com.ibm.omsalertdashboard.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.ibm.omsalertdashboard.model.Master;

@Repository
public interface MasterRepository extends MongoRepository<Master, String>{

}
