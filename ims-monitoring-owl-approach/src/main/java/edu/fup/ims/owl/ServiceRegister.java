package edu.fup.ims.owl;

import java.io.File;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.AddAxiom;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAnnotation;
import org.semanticweb.owlapi.model.OWLAnnotationObjectVisitorEx;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLIndividual;
import org.semanticweb.owlapi.model.OWLLiteral;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;
import org.semanticweb.owlapi.profiles.OWL2DLProfile;
import org.semanticweb.owlapi.profiles.OWLProfileReport;
import org.semanticweb.owlapi.profiles.OWLProfileViolation;
import org.semanticweb.owlapi.reasoner.InferenceType;
import org.semanticweb.owlapi.reasoner.Node;
import org.semanticweb.owlapi.reasoner.NodeSet;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.reasoner.OWLReasonerFactory;
import org.semanticweb.owlapi.util.OWLObjectVisitorExAdapter;
import org.semanticweb.owlapi.vocab.PrefixOWLOntologyFormat;

import com.clarkparsia.pellet.owlapiv3.PelletReasonerFactory;

import edu.fup.ims.events.ParameterValueBinding;
import edu.fup.ims.events.ServiceCallEvent;

/**
 * 
 * @author Luis Eraso
 *
 */
public class ServiceRegister {
	
	static final private Logger log = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
	static final String soa_uri = "http://www.semanticweb.org/ontologies/2010/01/core-soa.owl";
	static final String soa_local_url = "src/main/resources/edu/fup/ims/ontologies/soa_v2.owl";
			
	OWLOntologyManager manager;
	OWLDataFactory dataFactory;
	OWLOntology ontology;
	OWLReasoner reasoner;
	String prefix;
	
	private static ServiceRegister singleton;
		
	public static ServiceRegister getInstance() {		
		if (singleton == null) {
			singleton = new ServiceRegister();
		}
		
		return singleton; 
	}
	
	// TODO Visibility was changed in order to update reference to ServiceRegister in evaluation execution
	public ServiceRegister() {
		init();
	}	
	
	
	/**
	 * Initiate the ServieRegister
	 * 1. Load SOA Ontology from local URL
	 * 2. Create OntologyManager and OWLDataFactory and assign to variable instances 
	 * 3. Verify general info from created Ontoloy
	 */
	public void init() {
		log.info("init()...");
		
		File fileOntology = new File(soa_local_url);
    	IRI iri = IRI.create( fileOntology );
    	
    	manager = OWLManager.createOWLOntologyManager();
		dataFactory = manager.getOWLDataFactory();
    	ontology = null;
    	prefix = null;
    	
		try {
			// Load the ontology
			ontology = manager.loadOntologyFromOntologyDocument(iri);
			System.out.println("Ontology Loaded...");
	        System.out.println("Document IRI:	" + iri);
	        System.out.println("Ontology    :	" + ontology.getOntologyID());
	        System.out.println("Format      :	" + manager.getOntologyFormat(ontology));
	        
			PrefixOWLOntologyFormat pm = (PrefixOWLOntologyFormat) manager.getOntologyFormat(ontology);
			prefix = pm.getDefaultPrefix();
			System.out.println("Prefix      :	" + prefix);
	        	        
			OWL2DLProfile profile = new OWL2DLProfile();
			OWLProfileReport report = profile.checkOntology( ontology );
			
			if( !report.getViolations().isEmpty() ) {				
				System.out.println("Testing OWL2DLProfile: ");				
				for(OWLProfileViolation violation : report.getViolations()) {
					System.out.println(violation);
				}
			}
						
		} catch (OWLOntologyCreationException e) {
			e.printStackTrace();
		
		}
		
		System.out.println();
	}
	
	public void initReasoner(){
		log.info("initReasoner()...");
		OWLReasonerFactory reasonerFactory = PelletReasonerFactory.getInstance();
		//reasonerFactory.createNonBufferingReasoner(ontology)
		reasoner = reasonerFactory.createReasoner(ontology);
		
		reasoner.precomputeInferences(InferenceType.CLASS_HIERARCHY);
				
		// We can determine if the ontology is actually consistent
		boolean result = reasoner.isConsistent();
		System.out.println("Ontology is consistent: "+result);
		
		Node<OWLClass> bottomNode = reasoner.getUnsatisfiableClasses();
		System.out.println("Unsatisfiable classes:");

		for (OWLClass cls : bottomNode.getEntitiesMinusBottom())
			System.out.println(labelFor(cls, ontology));
		
		System.out.println();
	}
	
	public void precomputeInferences(){
		log.info("precomputeInferences()...");
		OWLReasonerFactory reasonerFactory = PelletReasonerFactory.getInstance();
		reasoner = reasonerFactory.createReasoner(ontology);
		reasoner.precomputeInferences(InferenceType.CLASS_HIERARCHY);
	}
	
	
	/**
	 * Serialize the ontology instance variable in a file.
	 * The URL File is defined by: parentPathName and childpathName
	 * 
	 * @param parentPathName
	 * @param childpathName
	 */
	public void saveOntologyAs(String parentPathName, String childpathName) {
		log.info("parentName: " +parentPathName +" childpathName: "+childpathName);
		
		File output = new File(parentPathName, childpathName);
		IRI savedIRI = IRI.create(output);
		
		try {
			manager.saveOntology(ontology, savedIRI);
		} catch (OWLOntologyStorageException e) {
			e.printStackTrace();
			
		}
		
		System.out.println();
	}
	
	/**
	 * Add service <strong>serviceName</strong> to ServiceRegister. 
	 * This create a service individual in SOA Ontology including 
	 * the mapping of ServiceContract and ServiceInterface.
	 * 
	 * <p>TODO At this moment, it is just defined for HealthService, 
	 * but in future definitions it should take the WSDL or WADL and
	 * create the corresponded SOA Ontology representation. 
	 *  
	 * @param serviceName
	 */
	public OWLIndividual addServiceIndividual(String serviceName){
		log.info("serviceName: "+serviceName);
		//Register vitalSingns service
		OWLIndividual vitalSigns = addIndividualToOWLClass(serviceName, "Service");
		OWLIndividual sc1 = addIndividualToOWLClass("sc1", "ServiceContract");
		OWLIndividual si1 = addIndividualToOWLClass("si1", "ServiceInterface");
		OWLIndividual userId = addIndividualToOWLClass("userId", "InformationType");
		OWLIndividual age = addIndividualToOWLClass("age", "InformationType");
		OWLIndividual weight = addIndividualToOWLClass("weight", "InformationType");
		OWLIndividual height = addIndividualToOWLClass("height", "InformationType");
		
		addObjectProperty("hasContract", vitalSigns, sc1);
		addObjectProperty("hasInterface", vitalSigns, si1);
		addObjectProperty("hasInput", si1, userId);
		addObjectProperty("hasOutput", si1, age);
		addObjectProperty("hasOutput", si1, weight);
		addObjectProperty("hasOutput", si1, height);
		
		//Create constraint and add it to InformationType individual
		OWLIndividual heightConstraint = addIndividualToOWLClass("heightConstraint", "IntegerRangeValueConstraint");		
		addDataProperty("hasMinValue", heightConstraint, 30);
		addDataProperty("hasMaxValue", heightConstraint, 250);
		
		addObjectProperty("hasConstraint", height, heightConstraint);
		
		System.out.println();
		
		return vitalSigns;
		
	}
	
	/**
	 * Add an InfoMessage (ServiceCallEvent individual) to HealthService registered in the SOA Ontology
	 * 
	 * <p>It includes values for all Input/Output InformationType parameters. 
	 * 
	 * @param serviceName
	 * @param infoMessage
	 */
	public void addInfoMessageToServive(String serviceName, String infoMessage) {
		log.info("serviceName: "+serviceName+" infoMessage: "+infoMessage);
		 
		//Add an information event to the vitalSigns service
		OWLIndividual message = addIndividualToOWLClass(infoMessage, "ServiceExecution");
		OWLIndividual service = dataFactory.getOWLNamedIndividual( IRI.create(prefix + serviceName) );
		addObjectProperty("generatedBy", message, service);
		
		//Adding userId input parameter		
		OWLIndividual pUserId = addIndividualToOWLClass(infoMessage+"_pUserId", "InputParameter");
		OWLIndividual userId = dataFactory.getOWLNamedIndividual( IRI.create(prefix + "userId") );
		addObjectProperty("hasType", pUserId, userId);
		addDataProperty("hasValue", pUserId , 820);
		addObjectProperty("hasParameter", message, pUserId);
		
		//adding age output parameter		
		OWLIndividual pAge = addIndividualToOWLClass(infoMessage+"_pAge", "OutputParameter");
		OWLIndividual age = dataFactory.getOWLNamedIndividual( IRI.create(prefix + "age") );
		addObjectProperty("hasType", pAge, age);
		addDataProperty("hasValue", pAge , 25);
		addObjectProperty("hasParameter", message, pAge);
		
		//adding height output parameter		
		OWLIndividual pHeight = addIndividualToOWLClass(infoMessage+"_pHeight", "OutputParameter");
		OWLIndividual height = dataFactory.getOWLNamedIndividual( IRI.create(prefix + "height") );
		addObjectProperty("hasType", pHeight, height);
		addDataProperty("hasValue", pHeight , 280); //This has a constraint violation.
		addObjectProperty("hasParameter", message, pHeight);
		
		//adding weight output parameter		
		OWLIndividual pWeight = addIndividualToOWLClass(infoMessage+"_pWeight", "OutputParameter");
		OWLIndividual weight = dataFactory.getOWLNamedIndividual( IRI.create(prefix + "weight") );
		addObjectProperty("hasType", pWeight, weight);
		addDataProperty("hasValue", pWeight , 61.8);
		addObjectProperty("hasParameter", message, pWeight);
		
		System.out.println();
	}
	
	public void addInfoMessageToServive(List<ServiceCallEvent> listEvents) {
		
		log.info("addInfoMessageToServive() listEvents.size: "+listEvents.size());
		
		for(ServiceCallEvent event :  listEvents){
			
			String serviceExecutionId = event.getServiceName()+event.getTimestamp().getTime();
					
			//Add an information event to the service
			OWLIndividual message = addIndividualToOWLClass(event.getServiceName()+event.getTimestamp(), "ServiceExecution");
			OWLIndividual service = dataFactory.getOWLNamedIndividual( IRI.create( prefix + event.getServiceName()) );
			addObjectProperty("generatedBy", message, service);
			
			//Adding input parameters
			for(ParameterValueBinding parameterValue : event.getInputs()) {
					
				OWLIndividual inputParameter = addIndividualToOWLClass(serviceExecutionId+"_p_"+parameterValue.getParameter(), "InputParameter");
				OWLIndividual typeInformation = dataFactory.getOWLNamedIndividual( IRI.create(prefix + parameterValue.getParameter() ) );
				addObjectProperty("hasType", inputParameter, typeInformation);
				addDataProperty("hasValue", inputParameter , Float.parseFloat(parameterValue.getDataValue()) );
				addObjectProperty("hasParameter", message, inputParameter);
								
			}
			
			//Adding output parameters
			for(ParameterValueBinding parameterValue : event.getOutputs()) {
					
				OWLIndividual outputParameter = addIndividualToOWLClass(serviceExecutionId+"_p_"+parameterValue.getParameter(), "OutputParameter");
				OWLIndividual typeInformation = dataFactory.getOWLNamedIndividual( IRI.create(prefix + parameterValue.getParameter() ) );
				addObjectProperty("hasType", outputParameter, typeInformation);
				addDataProperty("hasValue", outputParameter , Float.parseFloat(parameterValue.getDataValue())  );
				addObjectProperty("hasParameter", message, outputParameter);
								
			}					
		}				
	}
		 
	
	//////////////////////////////////////////////////////////////////////////////////////////
	///		Atomic functions for easy management of OWL Individuals			//
	//////////////////////////////////////////////////////////////////////////////////////////	
	
	/**
	 * List all the named individuals defined in the ontology instance variable
	 * 
	 */
	public void printIndividuals(){
		log.info("printIndividuals()...");
		
		int individuals = ontology.getIndividualsInSignature().size();		
		System.out.println("Number of individuals: " + individuals);
		
		for (OWLNamedIndividual ind : ontology.getIndividualsInSignature()){				
			System.out.println( ind.getIRI() );
		}
		
		System.out.println();
	}
	
	/**
	 * 
	 * Print all classes that have associated individuals.
	 * For each of these classes it prints all individuals 
	 * including its object properties and values
	 */
	public void printAllIndividals(){
		log.info("printAllIndividals()...");
		
		// for each class, look up the instances
		for (OWLClass owlClass : ontology.getClassesInSignature()) {
			
            assert owlClass != null;                        
            NodeSet<OWLNamedIndividual> instances = reasoner.getInstances(owlClass, true);
            
            //Just print OWL Classes that have individuals
            if( !instances.isEmpty() ) {
            	System.out.println("OWLClass: "+ owlClass);
            }            
            
            for (OWLNamedIndividual i : instances.getFlattened()) {
            	
            	assert i != null;
            	System.out.println("	ind: "+ i);
                                
                // look up all property assertions
                for (OWLObjectProperty op : ontology.getObjectPropertiesInSignature()) {
                	                	
                    assert op != null;
                    NodeSet<OWLNamedIndividual> petValuesNodeSet = reasoner.getObjectPropertyValues(i, op);
                    
                    //Just print OWL Object Properties that have individuals
                    if( !petValuesNodeSet.isEmpty() ) {
                    	System.out.println("		prop: "+ op);
                    }                    
                    
                    for (OWLNamedIndividual value : petValuesNodeSet.getFlattened()) {
                    	System.out.println("			value: "+ value);
                        //assertNotNull(value);
                    }
                }
            }
		
		}
		
		System.out.println();
	}
	
	
	public int printAllIndividualsFromClass(String className){
		log.info("printAllIndividualsFromClass() className: "+className);
		
		int numberOfInstances = 0;
		
		OWLClass owlClass = dataFactory.getOWLClass( IRI.create(prefix + className) );
						
            assert className != null;                        
            NodeSet<OWLNamedIndividual> instances = reasoner.getInstances(owlClass, true);
            
            //Just print OWL Classes that have individuals
            if( !instances.isEmpty() ) {
            	System.out.println("OWLClass: "+ owlClass);            	
            }            
            
            for (OWLNamedIndividual i : instances.getFlattened()) {
            	
            	assert i != null;
            	System.out.println("	ind: "+ i);
            	numberOfInstances++;                
                // look up all property assertions
                for (OWLObjectProperty op : ontology.getObjectPropertiesInSignature()) {
                	                	
                    assert op != null;
                    NodeSet<OWLNamedIndividual> petValuesNodeSet = reasoner.getObjectPropertyValues(i, op);
                    
                    //Just print OWL Object Properties that have individuals
                    if( !petValuesNodeSet.isEmpty() ) {
                    	System.out.println("		prop: "+ op);
                    }                    
                    
                    for (OWLNamedIndividual value : petValuesNodeSet.getFlattened()) {
                    	System.out.println("			value: "+ value);
                        //assertNotNull(value);
                    }
                }           		
		}
            
        System.out.println();
        return numberOfInstances;
	}
	// printIndividualsFromBasicClassExpression("generatedBy", "Service") --> OWL Named individuals generated by a service
	public void printIndividualsFromBasicClassExpression(String objectPorperty, String className){
		log.info("printIndividualsFromBasicClassExpression()...");		
		
		OWLObjectProperty owlObjectPorperty = dataFactory.getOWLObjectProperty(IRI.create(prefix + objectPorperty ));
		OWLClass owlClass = dataFactory.getOWLClass( IRI.create(prefix + className ) );
		
		OWLClassExpression owlClassExpression = dataFactory.getOWLObjectSomeValuesFrom(owlObjectPorperty, owlClass);		
		
		NodeSet<OWLNamedIndividual> list= reasoner.getInstances(owlClassExpression, false);
		for(Node<OWLNamedIndividual> node : list.getNodes()){
			System.out.println(node);
		}
	}
		
	//////////////////////////////////////////////////////////////////////////////////////////
	///		Atomic functions for easy management of OWL Classes and Properties.				//
	//////////////////////////////////////////////////////////////////////////////////////////	
	public OWLIndividual addIndividualToOWLClass(String owlIndividualName, String owlClassName) {
		
		OWLClass owlClass = dataFactory.getOWLClass( IRI.create(prefix + owlClassName) );
		OWLIndividual owlIndividual = dataFactory.getOWLNamedIndividual( IRI.create(prefix + owlIndividualName) );
		OWLAxiom owlAxiom = dataFactory.getOWLClassAssertionAxiom(owlClass, owlIndividual);
		manager.applyChange( new AddAxiom(ontology, owlAxiom) );
		
		return owlIndividual;
	}
	
	public void addObjectProperty(String objectProperty, OWLIndividual domain, OWLIndividual range) {
		
		OWLObjectProperty property = dataFactory.getOWLObjectProperty(IRI.create(prefix + objectProperty));
		OWLAxiom owlAxiom = dataFactory.getOWLObjectPropertyAssertionAxiom(property, domain, range);
		manager.applyChange(new AddAxiom(ontology, owlAxiom));
		
	}
	
	//addDataProperty...
	public void addDataProperty(String dataProperty, OWLIndividual domain, boolean value) {		
		OWLDataProperty property = dataFactory.getOWLDataProperty(IRI.create(prefix + dataProperty));
		OWLAxiom owlAxiom = dataFactory.getOWLDataPropertyAssertionAxiom(property, domain, value);		
		manager.applyChange(new AddAxiom(ontology, owlAxiom));		
	}
	public void addDataProperty(String dataProperty, OWLIndividual domain, int value) {		
		OWLDataProperty property = dataFactory.getOWLDataProperty(IRI.create(prefix + dataProperty));
		OWLAxiom owlAxiom = dataFactory.getOWLDataPropertyAssertionAxiom(property, domain, value);		
		manager.applyChange(new AddAxiom(ontology, owlAxiom));		
	}
	public void addDataProperty(String dataProperty, OWLIndividual domain, float value) {		
		OWLDataProperty property = dataFactory.getOWLDataProperty(IRI.create(prefix + dataProperty));
		OWLAxiom owlAxiom = dataFactory.getOWLDataPropertyAssertionAxiom(property, domain, value);		
		manager.applyChange(new AddAxiom(ontology, owlAxiom));		
	}
	public void addDataProperty(String dataProperty, OWLIndividual domain, double value) {		
		OWLDataProperty property = dataFactory.getOWLDataProperty(IRI.create(prefix + dataProperty));
		OWLAxiom owlAxiom = dataFactory.getOWLDataPropertyAssertionAxiom(property, domain, value);		
		manager.applyChange(new AddAxiom(ontology, owlAxiom));		
	}
	public void addDataProperty(String dataProperty, OWLIndividual domain, String value) {		
		OWLDataProperty property = dataFactory.getOWLDataProperty(IRI.create(prefix + dataProperty));
		OWLAxiom owlAxiom = dataFactory.getOWLDataPropertyAssertionAxiom(property, domain, value);		
		manager.applyChange(new AddAxiom(ontology, owlAxiom));		
	}	
	
	private String labelFor(OWLEntity clazz, OWLOntology o) {
		LabelExtractor le = new LabelExtractor();
		
		Set<OWLAnnotation> annotations = clazz.getAnnotations(o);
		for (OWLAnnotation anno : annotations) {
			String result = anno.accept(le);
				if (result != null) {
				return result;
			}
		}
		
		return clazz.getIRI().toString();
	}
	
	private class LabelExtractor extends OWLObjectVisitorExAdapter<String> implements OWLAnnotationObjectVisitorEx<String> {	
		@Override
		public String visit(OWLAnnotation annotation) {
			if (annotation.getProperty().isLabel()) {
				OWLLiteral c = (OWLLiteral) annotation.getValue();
				return c.getLiteral();
			}
			return null;
		}
	}

}
