package edu.fup.ims.app;

import edu.fup.ims.owl.ServiceRegister;

public class AppTest1 {
	
	ServiceRegister serviceRegister;
	
	public static void main(String[] args) {
		
		//Initiate service register				
		ServiceRegister serviceRegister = ServiceRegister.getInstance();
		
		//Add serviceHealth to service register	
		String vitalSigns = "HealthService";		
		serviceRegister.addServiceIndividual(vitalSigns);	
		
		//addInfoMessageToServive
		serviceRegister.addInfoMessageToServive(vitalSigns,"m1");
		
		//Save changes to new serialized ontology
		serviceRegister.saveOntologyAs("src/main/resources/edu/fup/ims/ontologies/", "saved_soa.owl");
		
		//Print all individuals
		//serviceRegister.printIndividuals();		
		
		//Reasoining over ontolgoy
		serviceRegister.initReasoner();
		
		serviceRegister.printAllIndividals();
		
		serviceRegister.printAllIndividualsFromClass("Exception");
		serviceRegister.printAllIndividualsFromClass("Warning");
		
	}
}