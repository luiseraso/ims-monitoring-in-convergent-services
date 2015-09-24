package edu.fup.ims.services;

import java.util.ArrayList;
import java.util.List;

public class ServiceInterface {
	
	List<InfoType> inputs;
	List<InfoType> outputs;	
	List<Constraint> constraints;
	
	public ServiceInterface(){		
		this.inputs = new ArrayList<>();
		this.outputs = new ArrayList<>();
		this.constraints = new ArrayList<>();
	}
	
	//Getters and setters...
	public List<InfoType> getInputs() {
		return inputs;
	}

	public void setInputs(List<InfoType> inputs) {
		this.inputs = inputs;
	}

	public List<InfoType> getOutputs() {
		return outputs;
	}

	public void setOutputs(List<InfoType> outputs) {
		this.outputs = outputs;
	}

	public List<Constraint> getConstraints() {
		return constraints;
	}

	public void setConstraints(List<Constraint> constraints) {
		this.constraints = constraints;
	}

}
