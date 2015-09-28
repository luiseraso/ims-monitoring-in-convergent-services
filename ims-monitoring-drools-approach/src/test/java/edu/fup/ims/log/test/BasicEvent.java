package edu.fup.ims.log.test;

public class BasicEvent {

	private final long id;
	private String message;
	private String generatedBy;
	private Type type;
	private int priority;

	public enum Type {
		INFO, WARNING, EXCEPTION
	}

	public BasicEvent(long id, String message, String generatedBy, Type type,
			int priority) {
		this.id = id;
		this.message = message;
		this.generatedBy = generatedBy;
		this.type = type;
		this.priority = priority;
	}
	
	  @Override
	  public String toString() {
	    return "EventLog [id='" + id + "', message='" + message + "', generatedBy='"
	        + generatedBy + "', type='" + type + "', priority='" + priority + "']";
	  }
	  
	
	//Getters % setters...
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getGeneratedBy() {
		return generatedBy;
	}

	public void setGeneratedBy(String generatedBy) {
		this.generatedBy = generatedBy;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

	public long getId() {
		return id;
	}
}
