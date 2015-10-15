package edu.fup.ims.events;

import java.util.ArrayList;
import java.util.List;

public class IncidenceRegister {
	
	private List<Event> eventList;
	
	public IncidenceRegister(){
		setEventList(new ArrayList<Event>()); 
		
	}

	public List<Event> getEventList() {
		return eventList;
	}

	public void setEventList(List<Event> eventList) {
		this.eventList = eventList;
	}

}
