package com.ibm.omsalertdashboard.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.ibm.omsalertdashboard.model.CoC_Prod;

@Repository
public interface Coc_ProdRepository extends MongoRepository<CoC_Prod, String>{

}
