package edu.fup.ims.services;

public class ConstraintTime extends Constraint {
	
	/**
	 * Maximum time of execution defines in mili-seconds
	 */
	int maxTimeExecution;	
	
	public ConstraintTime(int maxTimeExecution){
		this.maxTimeExecution = maxTimeExecution;		
	}

}
