package edu.fup.ims.events;

import java.util.Date;
import java.util.List;

public class ServiceCallEvent extends InformationEvent {
	
	Date timestamp;
	String serviceName;
	List<ParameterValueBinding> inputs;
	List<ParameterValueBinding> outputs;
	
	public ServiceCallEvent(Date timestamp, String serviceName, List<ParameterValueBinding> inputs, List<ParameterValueBinding> outputs){
		this.timestamp = timestamp;
		this.serviceName = serviceName;
		this.inputs = inputs;
		this.outputs = outputs;
	}
	
	@Override
	public String toString() {
		
		StringBuilder strBuilder = new StringBuilder();
		
		strBuilder.append("ServiceCallEvent ["
				+ "timestamp='" + timestamp
				+ "', serviceName='" + serviceName 
				+ "', \ninputs={" );		
				for (ParameterValueBinding it : inputs){
					strBuilder.append("\n\t"+it);
				}
				strBuilder.append("\n\t}, ");
				strBuilder.append("\noutputs={");
				for (ParameterValueBinding it : outputs){
					strBuilder.append("\n\t"+it);
				}
				strBuilder.append("\n\t}\n]");
				
		return strBuilder.toString();
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public List<ParameterValueBinding> getInputs() {
		return inputs;
	}

	public void setInputs(List<ParameterValueBinding> inputs) {
		this.inputs = inputs;
	}

	public List<ParameterValueBinding> getOutputs() {
		return outputs;
	}

	public void setOutputs(List<ParameterValueBinding> outputs) {
		this.outputs = outputs;
	}
		

}
