package edu.fup.ims.events;

/*
 * http://books.xmlschemata.org/relaxng/relax-CHP-19.html
 * http://www.w3.org/TR/xmlschema-2/ 
 * http://www.w3.org/TR/xmlschema11-2/
 */
public class ParameterValueBinding {
	
	String parameter;
	String dataValue;
	Object objectValue;

	public ParameterValueBinding(String parameter, String dataValue){
		this.parameter = parameter;
		this.dataValue = dataValue;
		this.objectValue = null;
		
	}
	
	public ParameterValueBinding(String parameter, Object objectValue){
		this.parameter = parameter;
		this.dataValue = null;
		this.objectValue = objectValue;
		
	}
	
	@Override
	public String toString() {
		return "ParameterValueBinding [parameter='" + parameter + "', dataValue='" + dataValue +  "', objectValue='" + objectValue +  "']";		
	}

	// Getters and setters...
	public String getParameter() {
		return parameter;
	}

	public void setParameter(String parameter) {
		this.parameter = parameter;
	}

	public String getDataValue() {
		return dataValue;
	}

	public void setDataValue(String dataValue) {
		this.dataValue = dataValue;
	}

	public Object getObjectValue() {
		return objectValue;
	}

	public void setObjectValue(Object objectValue) {
		this.objectValue = objectValue;
	}	
}
