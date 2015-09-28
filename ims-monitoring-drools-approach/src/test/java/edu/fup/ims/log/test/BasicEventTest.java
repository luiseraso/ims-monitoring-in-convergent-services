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

/**
 * Ejemplos tomados de:
 * http://www.vogella.com/tutorials/JavaLibrary-Gson/article.html
 * http://www.adictosaltrabajo.com/tutoriales/tutoriales.php?pagina=GsonJavaJSON
 * 
 * @author Luis Eraso
 *
 */
public class BasicEventTest {
	
	static final String SERVER_LOG_URL = "src\\test\\resources\\edu\\fup\\ims\\log\\ServerLog.log";

	public static void main(String[] args) {

		Path filePath = Paths.get(SERVER_LOG_URL);
				
		try (BufferedReader reader = Files.newBufferedReader(filePath)) {

			Gson gson = new GsonBuilder().setPrettyPrinting().create(); //Gson gson = new Gson();
			
			Type type = new TypeToken<List<BasicEvent>>(){}.getType();
			List<BasicEvent> fromJson = gson.fromJson(reader, type);
			
		    for (BasicEvent event : fromJson) {
		      System.out.println(event);
		    }
					
		} catch (IOException e) {
			//e.printStackTrace();
			System.err.println("Error while opening "+ filePath.toUri());
			
		}
		
		/*
		List<EventLog> list = new ArrayList<EventLog>();
		for (int i = 0; i < 20; i++) {
			list.add(new EventLog(i, "Test1", "Test2", EventLog.Type.INFO, 10));
		}
				
		Gson gson = new GsonBuilder().setPrettyPrinting().create(); // Gson gson = new Gson();
		Type type = new TypeToken<List<EventLog>>(){}.getType();
		String json = gson.toJson(list, type);
		System.out.println(json);

		List<EventLog> fromJson = gson.fromJson(json, type);
		for (EventLog task : fromJson){
			System.out.println(task);
		}
		*/

	}
	
	
	
	
}
