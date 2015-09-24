package edu.fup.ims.events;

import edu.fup.ims.services.ConstraintNumberRangeValue;

public class NumberRangeValueExceptionEvent<T extends Number> extends ExceptionEvent{
	
	String serviceName;
	ParameterValueBinding value;
	ConstraintNumberRangeValue<T> constraint;
	
	
	public NumberRangeValueExceptionEvent(
			String serviceName, 
			ConstraintNumberRangeValue<T> constraint, 
			ParameterValueBinding value) {
		
		this.serviceName = serviceName;
		this.constraint = constraint;
		this.value = value;
		
	}

	//Getters and setters...
	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public ParameterValueBinding getValue() {
		return value;
	}

	public void setValue(ParameterValueBinding value) {
		this.value = value;
	}
	
	public ConstraintNumberRangeValue<T> getConstraint() {
		return constraint;
	}

	public void setConstraint(ConstraintNumberRangeValue<T> constraint) {
		this.constraint = constraint;
	}
}
