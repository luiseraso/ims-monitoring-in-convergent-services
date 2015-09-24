package edu.fup.ims.rules.test;

import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;

import edu.fup.ims.services.Constraint;
import edu.fup.ims.services.ConstraintNumberRangeValue;
import edu.fup.ims.services.ConstraintTime;
import edu.fup.ims.services.InfoType;
import edu.fup.ims.services.Service;
import edu.fup.ims.services.ServiceContract;
import edu.fup.ims.services.ServiceInterface;
import edu.fup.ims.services.ServiceRegister;
import edu.fup.ims.services.WebService;

public class BasicRulesTest {

	public static void main(String[] args) {		
		
	    KieServices ks = KieServices.Factory.get();
	    KieContainer kc = ks.getKieClasspathContainer();
	    KieSession ksession = kc.newKieSession("BasicRulesKS");
	    
	    //Service register
	    ServiceContract sc = new ServiceContract();	    
	    ServiceInterface si = new ServiceInterface();
	    
	    InfoType id = 
	    		new InfoType("id", InfoType.DataType.INT);
	    InfoType age = 
	    		new InfoType("age", InfoType.DataType.INT);
	    InfoType weight = 
	    		new InfoType("weight", InfoType.DataType.FLOAT);
	    InfoType height = 
	    		new InfoType("height", InfoType.DataType.FLOAT);
	    
	    ConstraintNumberRangeValue<Integer> ageConstraint = 
	    		new ConstraintNumberRangeValue<Integer>(age, 0, 120 );
	    ConstraintNumberRangeValue<Float> heightConstraint = 
	    		new ConstraintNumberRangeValue<Float>(height, 30.0f, 250.0f );
	    	    
	    si.getInputs().add(id);
	    si.getOutputs().add(age);	 
	    si.getOutputs().add(weight);
	    si.getOutputs().add(height);
	    si.getConstraints().add(ageConstraint);
	    si.getConstraints().add(heightConstraint);
	    
	    //Constraint of time should be in Legal Agreements Aspects...
	    Constraint constraintTime01 = new ConstraintTime(20);
	    si.getConstraints().add(constraintTime01);
	    
	    Service bhSingns = new WebService("BasicHealthSings", sc, si);
   
	    ServiceRegister serviceRegister = ServiceRegister.getServiceRegister();
	    serviceRegister.addService(bhSingns);
	    
	    ksession.insert( bhSingns );
	    
	    ksession.fireAllRules();
	    
	    
	}

}
