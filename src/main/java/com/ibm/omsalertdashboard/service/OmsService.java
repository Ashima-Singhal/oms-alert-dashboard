package com.ibm.omsalertdashboard.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ibm.omsalertdashboard.model.CoC_IKS;
import com.ibm.omsalertdashboard.model.CoC_Prod;
import com.ibm.omsalertdashboard.model.Master;
import com.ibm.omsalertdashboard.model.MasterEvents;
import com.ibm.omsalertdashboard.repository.Coc_IKSRepository;
import com.ibm.omsalertdashboard.repository.Coc_ProdRepository;
import com.ibm.omsalertdashboard.repository.MasterRepository;

@Service
public class OmsService {

	private final MasterRepository masterRepository;
	private final QueryService queryService;
	private final Coc_ProdRepository prodRepository;
	private final Coc_IKSRepository iksRepository;
	
	public OmsService(final MasterRepository masterRepository, final QueryService queryService,
					  final Coc_ProdRepository prodRepository, final Coc_IKSRepository iksRepository) {
		super();
		this.masterRepository = masterRepository;
		this.queryService = queryService;
		this.prodRepository = prodRepository;
		this.iksRepository = iksRepository;
	}


	public List<Master> getLatestRecordMaster(){
		List<Master> list = masterRepository.findAll();
		//System.out.println("output-"+list.get(list.size()-1));  
		return list;
		//System.out.println("events list$$$$"+queryService.getEvents(latestRecord.toString())); 
		//return null;
		//System.out.println("fetched value from db$$$" + String.valueOf(latestRecord));  
	}
	
	public List<CoC_Prod> getLatestRecordProd() {
		List<CoC_Prod> list = prodRepository.findAll();
		return list;
	}
	
	public List<CoC_IKS> getLatestRecordIks() {
		List<CoC_IKS> list = iksRepository.findAll();
		return list;
	}
	
}
