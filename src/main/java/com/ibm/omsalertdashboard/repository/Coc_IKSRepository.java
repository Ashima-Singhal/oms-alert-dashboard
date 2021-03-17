package com.ibm.omsalertdashboard.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.ibm.omsalertdashboard.model.CoC_IKS;

@Repository
public interface Coc_IKSRepository extends MongoRepository<CoC_IKS, String>{

}
