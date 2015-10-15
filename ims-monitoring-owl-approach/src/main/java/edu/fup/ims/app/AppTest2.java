package edu.fup.ims.app;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;

import edu.fup.ims.commons.Util;
import edu.fup.ims.events.ServiceCallEvent;
import edu.fup.ims.owl.ServiceRegister;

public class AppTest2 {
	
	static final String SERVER_LOG_URL = "src\\main\\resources\\edu\\fup\\ims\\log\\ServiceCallEvents_";
	static final Path PATH = Paths.get("src/main/resources/edu/fup/ims/evaluation/output.txt");
	
	ServiceRegister serviceRegister;
	String serviceName ="HealthService";
	
	public static void main(String[] args) {
		AppTest2 appTest = new AppTest2();
		
		try (PrintWriter writer = new PrintWriter(Files.newBufferedWriter(PATH))) {
			writer.print("Messages, Exceptions, Warnings, InitServiceRegister, InitReasoner, AddServiceIndividual,  t1, t2, t3");
		} catch (IOException e) {		
			e.printStackTrace();
		}
			
		appTest.addServiceExecutionEvents("10-0");
		appTest.addServiceExecutionEvents("10-05");
		appTest.addServiceExecutionEvents("10-10");
		appTest.addServiceExecutionEvents("50-0");
		appTest.addServiceExecutionEvents("50-05");
		appTest.addServiceExecutionEvents("50-10");
		appTest.addServiceExecutionEvents("50-50");
		appTest.addServiceExecutionEvents("100-0");
		appTest.addServiceExecutionEvents("100-005");
		appTest.addServiceExecutionEvents("100-010");
		appTest.addServiceExecutionEvents("100-050");
		appTest.addServiceExecutionEvents("100-100");
		
	}
	
	public void addOneMessageAndOneError(){
				
		long timer_0, timer_1, timer_2, timer_3;
		
		//Add Messages to Ontology
		timer_0 = System.currentTimeMillis();
		serviceRegister.addInfoMessageToServive(serviceName,"m1");
		timer_1 = System.currentTimeMillis();
		
		//Call reasoner
		this.precomputeInferences();
		timer_2 = System.currentTimeMillis();
		
		//Look for Exception and Warning events
		serviceRegister.printAllIndividualsFromClass("Exception");		
		serviceRegister.printAllIndividualsFromClass("Warning");		
		timer_3 = System.currentTimeMillis();
		
		//Saver results in log file
		try (PrintWriter writer = new PrintWriter(Files.newBufferedWriter(PATH, StandardOpenOption.APPEND))) {
			writer.println("----------oneMessageAndOneError----------");
		    writer.println("t1, t2, t3");
		    writer.println((timer_1-timer_0)+", "+(timer_2-timer_1)+", "+(timer_3-timer_2));
		    writer.println();
		    
		} catch (IOException e) {		
			e.printStackTrace();
		}
		
		System.out.println();
	}
	
	public void addServiceExecutionEvents(String path){
				
		long initRegisterTime = initServiceRegister();
		long initReasonerTime = initReasoner();
		long initServiceTime = addServiceIndividual();
		
		long timer_0, timer_1, timer_2, timer_3;
		
		timer_0 = System.currentTimeMillis();
		List<ServiceCallEvent> eventList = Util.getServiceCallEvents(SERVER_LOG_URL+path+".log");
		System.out.println("number of events: "+eventList.size());
		serviceRegister.addInfoMessageToServive( eventList );
		timer_1 = System.currentTimeMillis();
		
		//Call reasoner
		this.precomputeInferences();
		timer_2 = System.currentTimeMillis();
		
		//Look for Exception and Warning events
		int exceptions = serviceRegister.printAllIndividualsFromClass("Exception");		
		int warnings = serviceRegister.printAllIndividualsFromClass("Warning");
		timer_3 = System.currentTimeMillis();
		
		try (PrintWriter writer = new PrintWriter(Files.newBufferedWriter(PATH, StandardOpenOption.APPEND))) {	
		    
		    writer.print("\n"+eventList.size()+", "+ exceptions+", "+ warnings +", "+initRegisterTime+", "+initReasonerTime+", "+initServiceTime+", "
		    		+(timer_1-timer_0)+", "+(timer_2-timer_1)+", "+(timer_3-timer_2));

		    
		} catch (IOException e) {		
			e.printStackTrace();
		}
		
		System.out.println();
	}
	
	//Test methods
	public long initServiceRegister(){
		long initTime = System.currentTimeMillis();
		//serviceRegister = ServiceRegister.getInstance();
		serviceRegister = new ServiceRegister();
		long endTime = System.currentTimeMillis();
		return endTime - initTime;
	}
		
	public long initReasoner(){
		long initTime = System.currentTimeMillis();
		serviceRegister.initReasoner();
		long endTime = System.currentTimeMillis();
		return endTime - initTime;
	}
	
	public long precomputeInferences(){
		long initTime = System.currentTimeMillis();
		serviceRegister.precomputeInferences();
		long endTime = System.currentTimeMillis();
		return endTime - initTime;
	}	
	
	public long addServiceIndividual(){
		long initTime = System.currentTimeMillis();
		serviceRegister.addServiceIndividual("HealthService");
		long endTime = System.currentTimeMillis();
		return endTime - initTime;
	}
	
}
