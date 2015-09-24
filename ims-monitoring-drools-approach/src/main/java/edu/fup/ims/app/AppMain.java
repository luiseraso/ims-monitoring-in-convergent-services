package edu.fup.ims.app;

import java.io.BufferedReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import edu.fup.ims.events.Event;
import edu.fup.ims.events.IncidenceRegister;
import edu.fup.ims.events.ServiceCallEvent;
import edu.fup.ims.services.Constraint;
import edu.fup.ims.services.ConstraintNumberRangeValue;
import edu.fup.ims.services.ConstraintTime;
import edu.fup.ims.services.InfoType;
import edu.fup.ims.services.Service;
import edu.fup.ims.services.ServiceContract;
import edu.fup.ims.services.ServiceInterface;
import edu.fup.ims.services.ServiceRegister;
import edu.fup.ims.services.WebService;

public class AppMain {
	
	static final String SERVER_LOG_URL = "src\\main\\resources\\edu\\fup\\ims\\log\\ServiceCallEvents_5-10.log";
	
	public static void main(String[] args) {
		
		long time_1, time_2, time_3, time_4;		
		
		time_1 = System.currentTimeMillis();
	    KieServices ks = KieServices.Factory.get();
	    KieContainer kc = ks.getKieClasspathContainer();
	    KieSession ksession = kc.newKieSession("MonitoringKS");
	    
	    time_2 = System.currentTimeMillis();
	    
	    ServiceContract sc = new ServiceContract();	    
	    ServiceInterface si = new ServiceInterface();
	    
	    InfoType id = 
	    		new InfoType("id", InfoType.DataType.INT);
	    InfoType age = 
	    		new InfoType("age", InfoType.DataType.INT);
	    InfoType weight = 
	    		new InfoType("weight", InfoType.DataType.FLOAT);
	    InfoType height = 
	    		new InfoType("height", InfoType.DataType.FLOAT);
	    
	    ConstraintNumberRangeValue<Integer> ageConstraint = 
	    		new ConstraintNumberRangeValue<Integer>(age, 0, 120 );
	    ConstraintNumberRangeValue<Float> heightConstraint = 
	    		new ConstraintNumberRangeValue<Float>(height, 30.0f, 250.0f );
	    	    
	    si.getInputs().add(id);
	    si.getOutputs().add(age);	 
	    si.getOutputs().add(weight);
	    si.getOutputs().add(height);
	    si.getConstraints().add(ageConstraint);
	    si.getConstraints().add(heightConstraint);
	    
	    //Constraint of time should be in Legal Agreements Aspects...
	    Constraint constraintTime01 = new ConstraintTime(20);
	    si.getConstraints().add(constraintTime01);
	    
	    Service bhSingns = new WebService("BasicHealthSings", sc, si);
   
	    ServiceRegister serviceRegister = ServiceRegister.getServiceRegister();
	    serviceRegister.addService(bhSingns);
	    
	    ksession.insert( bhSingns );
	    
	    IncidenceRegister incidenceRegister = new IncidenceRegister();
	    ksession.insert( incidenceRegister );
	    
	    time_3 = System.currentTimeMillis();
		
	    List<ServiceCallEvent> serviceCallEvent = getServiceCallEvents();
	    
	    for(ServiceCallEvent item: serviceCallEvent){
	    	 ksession.insert( item );
	    }
	   
	    ksession.fireAllRules();
	    
	    time_4 = System.currentTimeMillis();
	    
	    System.out.print("Knowledge-Base creation time: ");
	    System.out.println(time_2 - time_1);
	    
	    System.out.print("Service register time: ");
	    System.out.println(time_3 - time_2);
	    
	    System.out.print("Inference engine execution time: ");
	    System.out.println(time_4 - time_3);
	    
	    
   	    
	    System.out.println("Number of incidets: "+ incidenceRegister.getEventList().size());
	    printIncidenceEvents( incidenceRegister.getEventList() );
	    // and then dispose the session
	    ksession.dispose();
	}
	
	public static List<ServiceCallEvent> getServiceCallEvents(){
		
		List<ServiceCallEvent> fromJson = null;		
		Path filePath = Paths.get(SERVER_LOG_URL);
		
		try (BufferedReader reader = Files.newBufferedReader(filePath)) {
			Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd hh:mm:ss.S").create();			
			Type type = new TypeToken<List<ServiceCallEvent>>(){}.getType();
			fromJson = gson.fromJson(reader, type);		
					
		} catch (IOException e) {
			//e.printStackTrace();
			System.err.println("Error while opening "+ filePath.toUri());			
		}
		
		return fromJson;
	}
	
	public static void printIncidenceEvents(List<Event> list){
	
		Gson gson = new GsonBuilder().setPrettyPrinting().create(); // Gson gson = new Gson();
		Type type = new TypeToken<List<Event>>(){}.getType();
		String json = gson.toJson(list, type);
		System.out.println(json);
	}
}
