package edu.fup.ims.commons;

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

public class Util {
	
	public static List<ServiceCallEvent> getServiceCallEvents(String serverLogUrl){
		
		List<ServiceCallEvent> fromJson = null;		
		Path filePath = Paths.get(serverLogUrl);
		
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
	
}
