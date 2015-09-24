package edu.fup.ims.services;

import java.util.Date;

public abstract class Service {
	
	private String serviceName;
	ServiceContract serviceContract;
	ServiceInterface serviceInterface;

	/* This information will be changed to legalAspects in ServiceContract class.
	int minTimeExecution;
	int maxTimeExecution;
	*/
	
	int numberOfExecutions;
	int mediaTimeExecution;
	
	int totalTime; //Used to calculate mediaTimeExecution;
	
	Date firstDeploy;
	Date activeSince;
	Date lastFail;
	
	public Service(){
		serviceContract = null;
		numberOfExecutions = 0;
		mediaTimeExecution = 0;
	}
	
	public Service(String serviceName, ServiceContract serviceContract, ServiceInterface serviceInterface){
		this();
		this.serviceName = serviceName;
		this.serviceContract = serviceContract;
		this.serviceInterface = serviceInterface;
		
	}

	// Getters and setters... 
	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public ServiceContract getServiceContract() {
		return serviceContract;
	}

	public void setServiceContract(ServiceContract serviceContract) {
		this.serviceContract = serviceContract;
	}

	public ServiceInterface getServiceInterface() {
		return serviceInterface;
	}

	public void setServiceInterface(ServiceInterface serviceInterface) {
		this.serviceInterface = serviceInterface;
	}
		
}
