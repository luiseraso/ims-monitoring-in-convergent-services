package edu.fup.ims.owl;

public class AppTest {
	
	ServiceRegister serviceRegister;
	
	public static void main(String[] args) {
		
		ServiceRegister serviceRegister = ServiceRegister.getInstance();
		
		String vitalSigns = "HealthService";		
		serviceRegister.addServiceIndividual(vitalSigns);						
		serviceRegister.listIndividuals();		
		serviceRegister.saveOntologyAs("src/main/resources/edu/fup/ims/ontologies/", "saved_soa.owl");
				
		//addInfoMessageToServive
		serviceRegister.addInfoMessageToServive(vitalSigns,"m1");
		serviceRegister.saveOntologyAs("src/main/resources/edu/fup/ims/ontologies/", "saved_soa.owl");
		
	}

}
