package edu.fup.ims.services;

public class InfoType {
	
	public enum DataType{ BOOLEAN, INT, FLOAT, MONEY, TIME, DATE, DATETIME }
	
	String name;
	DataType dataType;	
	
	public InfoType(String name, DataType dataType){
		this.name = name;
		this.dataType = dataType;
	}

	// Getters and setters...
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public DataType getDataType() {
		return dataType;
	}

	public void setDataType(DataType dataType) {
		this.dataType = dataType;
	}
	
}
