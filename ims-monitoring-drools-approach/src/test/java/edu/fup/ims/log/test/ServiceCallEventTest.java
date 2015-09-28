package edu.fup.ims.log.test;

import java.io.BufferedReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import edu.fup.ims.events.ServiceCallEvent;

public class ServiceCallEventTest {

	static final String SERVER_LOG_URL = "src\\test\\resources\\edu\\fup\\ims\\log\\ServiceCallEvents.log";

	public static void main(String[] args) {

		Path filePath = Paths.get(SERVER_LOG_URL);

		/*
		ServiceCallEvent event = new ServiceCallEvent(new Date(), "VitalSingns", null, null);
		//System.out.println(event);
		
		//Gson gson = new Gson();
		
		Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd hh:mm:ss.S").create();
	    final String representacionJSON = gson.toJson(event);
	    
	    System.out.println("\n"+representacionJSON);
		*/		
		
		try (BufferedReader reader = Files.newBufferedReader(filePath)) {

			Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd hh:mm:ss.S").create(); //Gson gson = new Gson();
			
			Type type = new TypeToken<List<ServiceCallEvent>>(){}.getType();
			List<ServiceCallEvent> fromJson = gson.fromJson(reader, type);
			
		    for (ServiceCallEvent event : fromJson) {
		      System.out.println(event);
		    }
					
		} catch (IOException e) {
			//e.printStackTrace();
			System.err.println("Error while opening "+ filePath.toUri());
			
		}

	}
	
	
	
	
}
