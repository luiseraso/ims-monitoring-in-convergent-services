package edu.fup.ims.services;

import java.util.ArrayList;
import java.util.List;

public class ServiceRegister {
	
	private static ServiceRegister singleton = null;	
	private List<Service> serviceList;
	
	private ServiceRegister(){
		serviceList = new ArrayList<Service>();
	}

	/**
	 * 
	 * @return ServiceRegister instance
	 */
	public static ServiceRegister getServiceRegister(){
		if(singleton == null){
			singleton = new ServiceRegister();			
		}
		return singleton;
		
	}
	
	
	public void addService(Service s){
		serviceList.add(s);
	}

	//Getters and setters... 
	public List<Service> getServiceList() {
		return serviceList;
	}

	public void setServiceList(List<Service> serviceList) {
		this.serviceList = serviceList;
	}
	

}
