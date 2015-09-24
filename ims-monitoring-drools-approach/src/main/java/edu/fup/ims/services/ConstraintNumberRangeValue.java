package edu.fup.ims.services;

public class ConstraintNumberRangeValue<T extends Number> extends Constraint {
	
	String type;
	
	T maxValue;
	T minValue;
	
	public ConstraintNumberRangeValue(InfoType informationType, T minValue, T maxValue){
		this.type = this.getClass().getSimpleName();
		this.informationType = informationType;
		this.minValue = minValue;
		this.maxValue = maxValue;
	}

	
	public  String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;;
	}

	public T getMaxValue() {
		return maxValue;
	}

	public void setMaxValue(T maxValue) {
		this.maxValue = maxValue;
	}

	public T getMinValue() {
		return minValue;
	}

	public void setMinValue(T minValue) {
		this.minValue = minValue;
	}
	


}
