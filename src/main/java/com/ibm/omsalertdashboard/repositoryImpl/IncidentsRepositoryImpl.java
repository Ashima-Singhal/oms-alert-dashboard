package com.ibm.omsalertdashboard.repositoryImpl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;


import com.ibm.omsalertdashboard.model.Events;
import com.ibm.omsalertdashboard.model.Incidents;
import com.ibm.omsalertdashboard.repository.IncidentsRepository;
import com.ibm.omsalertdashboard.service.QueryService;

public class IncidentsRepositoryImpl implements IncidentsRepository{

	@Autowired
	private MongoTemplate mongoTemplate;
	
	@Autowired
	private static final Logger LOG = LoggerFactory.getLogger(IncidentsRepositoryImpl.class);
	
	@Override
	public <S extends Incidents> List<S> saveAll(Iterable<S> entities) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Incidents> findAll() {
		// TODO Auto-generated method stub
		return mongoTemplate.findAll(Incidents.class, "incidents");
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
	public List<Incidents> findByName(String name) {
		// TODO Auto-generated method stub
		//List<Incidents> list = new ArrayList<>();
		Query query = new Query();
		query.addCriteria(Criteria.where("name").is(name));
		//System.out.println("repo impl----"+mongoTemplate.find(query, Incidents.class)); 
		return mongoTemplate.find(query, Incidents.class); 
	}

	//method to insert events in the form of json in db
	@Override
	public void updateJsonList(Incidents incidents, String name,boolean flag) {
		// do this here only if inserting data for the first time
		if(!flag) {
			List<Events> events = incidents.getResults().get(0).get("events");
			//Map<Long,Events> eventMap = new HashMap<>();//to keep track of duplicate events sent by api
			for(Events event:events) {
//				if(!eventMap.containsKey(event.getIncident_id())) {
//					eventMap.put(event.getIncident_id(), event);
//					if(eventMap.get(event.getIncident_id()).getCurrent_state().equalsIgnoreCase("closed"))
//						eventMap.get(event.getIncident_id()).setEndTimestamp(event.getTimestamp()); 
//				}
				
				if(event.getCurrent_state().equalsIgnoreCase("closed")) 
					event.setEndTimestamp(event.getTimestamp()); //if event is closed end timestamp = timestamp
			}
//			events.clear();
//			for(Long key:eventMap.keySet()) {
//				events.add(eventMap.get(key)); 
//			}
			incidents.getResults().get(0).put("events", events);
		}
		
		Query query = new Query();
		query.addCriteria(Criteria.where("name").is(name));
		
		Update update = new Update();
		update.set("result", incidents.getResults());
		update.set("performanceStats", incidents.getPerformanceStats());
		update.set("metadata", incidents.getMetadata());
		
		mongoTemplate.upsert(query, update, Incidents.class);
	}

	@Override
	public void updateJsonList(Incidents incidents, String name) {
		Query query = new Query();
		query.addCriteria(Criteria.where("name").is(name));
		
		Update update = new Update();
		update.set("result", incidents.getResults());
		update.set("performanceStats", incidents.getPerformanceStats());
		update.set("metadata", incidents.getMetadata());
		
		mongoTemplate.upsert(query, update, Incidents.class);
	}

	@Override
	//generic code for pre prod and prod events
	//first insert all open events in map
	//check if closed event has corresponding open event then update
	//otherwise simply insert in map
	public void populate(Incidents incidents, String name) {
		List<Events> eventList = incidents.getResults().get(0).get("events");
		Map<Long, Events> eventMap = new HashMap<>();
		
		//insert open events in the map
		for(Events event: eventList) {
			if(event.getCurrent_state().equalsIgnoreCase("open")) {
				LOG.info("Open event = "+event.getIncident_id()); 
				eventMap.put(event.getIncident_id(), event);
			}
		}
		Set<Long> eventSet = new HashSet<>();//to keep track of duplicate events
		//updating closed events
		for(Events event:eventList) {
			if(event.getCurrent_state().equalsIgnoreCase("closed") && eventMap.containsKey(event.getIncident_id()) && !eventSet.contains(event.getIncident_id())) {  
				Events e = eventMap.get(event.getIncident_id());
				e.setCurrent_state(event.getCurrent_state());
				e.setEndTimestamp(event.getTimestamp());
				eventMap.put(event.getIncident_id(), e);
				//eventMap.get(event.getIncident_id()).setCurrent_state(event.getCurrent_state()); //update current state
				//eventMap.get(event.getIncident_id()).setEndTimestamp(event.getTimestamp()); //update end time
				LOG.info("Event "+event.getIncident_id()+" updated.");
				LOG.info("Start time = "+eventMap.get(event.getIncident_id()).getTimestamp()+" End time = "+eventMap.get(event.getIncident_id()).getEndTimestamp()); 
			}
			else if(event.getCurrent_state().equalsIgnoreCase("closed") && !eventSet.contains(event.getIncident_id())) {
				eventMap.put(event.getIncident_id(), event);
				eventMap.get(event.getIncident_id()).setEndTimestamp(event.getTimestamp()); 
				LOG.info("Event "+event.getIncident_id()+" added");
			}
			eventSet.add(event.getIncident_id());//adding event which has been processed
		}
		eventList.clear();
		for(Long key:eventMap.keySet()) {
			eventList.add(eventMap.get(key));
		}
		incidents.getResults().get(0).put("events", eventList);
		
		//insert incidents in db
		Query query = new Query();
		query.addCriteria(Criteria.where("name").is(name));
		
		Update update = new Update();
		update.set("name", name);
		update.set("result", incidents.getResults());
		update.set("performanceStats", incidents.getPerformanceStats());
		update.set("metadata", incidents.getMetadata());
		
		mongoTemplate.upsert(query, update, Incidents.class);
	}
	
	

}
