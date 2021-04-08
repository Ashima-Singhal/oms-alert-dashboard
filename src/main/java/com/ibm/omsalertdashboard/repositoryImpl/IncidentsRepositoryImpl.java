package com.ibm.omsalertdashboard.repositoryImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import com.ibm.omsalertdashboard.model.CoC_IKS;
import com.ibm.omsalertdashboard.model.CoC_Prod;
import com.ibm.omsalertdashboard.model.Incidents;
import com.ibm.omsalertdashboard.model.Master;
import com.ibm.omsalertdashboard.repository.IncidentsRepository;

public class IncidentsRepositoryImpl implements IncidentsRepository{

	@Autowired
	private MongoTemplate mongoTemplate;
	
	@Override
	public <S extends Incidents> List<S> saveAll(Iterable<S> entities) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Incidents> findAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Incidents> findAll(Sort sort) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <S extends Incidents> S insert(S entity) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <S extends Incidents> List<S> insert(Iterable<S> entities) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <S extends Incidents> List<S> findAll(Example<S> example) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <S extends Incidents> List<S> findAll(Example<S> example, Sort sort) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Page<Incidents> findAll(Pageable pageable) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <S extends Incidents> S save(S entity) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Optional<Incidents> findById(String id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean existsById(String id) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Iterable<Incidents> findAllById(Iterable<String> ids) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long count() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void deleteById(String id) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void delete(Incidents entity) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteAll(Iterable<? extends Incidents> entities) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteAll() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public <S extends Incidents> Optional<S> findOne(Example<S> example) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <S extends Incidents> Page<S> findAll(Example<S> example, Pageable pageable) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <S extends Incidents> long count(Example<S> example) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public <S extends Incidents> boolean exists(Example<S> example) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void updateMasterIncidents(Master master, String name) {
		// TODO Auto-generated method stub
		Query query = new Query();
		query.addCriteria(Criteria.where("name").is(name));
		
		Update update = new Update();
		update.set("incidents", master);
		
		mongoTemplate.upsert(query, update, Incidents.class);
	}

	@Override
	public void updateCocProdIncidents(CoC_Prod prod, String name) {
		// TODO Auto-generated method stub
		Query query = new Query();
		query.addCriteria(Criteria.where("name").is(name));
		
		Update update = new Update();
		update.set("incidents", prod);
		
		mongoTemplate.upsert(query, update, Incidents.class);
	}

	@Override
	public void updateCocIksIncidents(CoC_IKS iks, String name) {
		// TODO Auto-generated method stub
		Query query = new Query();
		query.addCriteria(Criteria.where("name").is(name));
		
		Update update = new Update();
		update.set("incidents", iks);
		
		mongoTemplate.upsert(query, update, Incidents.class);
	}

	@Override
	public List<Incidents> findByName(String name) {
		// TODO Auto-generated method stub
		//List<Incidents> list = new ArrayList<>();
		Query query = new Query();
		query.addCriteria(Criteria.where("name").is(name));
		//System.out.println("repo impl----"+mongoTemplate.find(query, Incidents.class)); 
		return mongoTemplate.find(query, Incidents.class); 
	}

	@Override
	public void updateJsonList(Incidents incidents, String name) {
		// TODO Auto-generated method stub
		Query query = new Query();
		query.addCriteria(Criteria.where("name").is(name));
		
		Update update = new Update();
		update.set("result", incidents.getResults());
		update.set("performanceStats", incidents.getPerformanceStats());
		update.set("metadata", incidents.getMetadata());
		
		mongoTemplate.upsert(query, update, Incidents.class);
	}

	
	

}
