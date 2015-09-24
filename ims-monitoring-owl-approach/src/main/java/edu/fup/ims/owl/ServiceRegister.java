package edu.fup.ims.owl;

import java.io.File;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.AddAxiom;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLIndividual;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;
import org.semanticweb.owlapi.profiles.OWL2DLProfile;
import org.semanticweb.owlapi.profiles.OWLProfileReport;
import org.semanticweb.owlapi.profiles.OWLProfileViolation;
import org.semanticweb.owlapi.vocab.PrefixOWLOntologyFormat;

/**
 * 
 * @author Luis Eraso
 *
 */
public class ServiceRegister {
	
	static final String soa_uri = "http://www.semanticweb.org/ontologies/2010/01/core-soa.owl";
	static final String soa_local_url = "src/main/resources/edu/fup/ims/ontologies/soa_v2.owl";
			
	OWLOntologyManager manager;
	OWLDataFactory dataFactory;
	OWLOntology ontology;
	String prefix;
	
	private static ServiceRegister singleton;
	
	public static ServiceRegister getInstance() {
		if (singleton == null) {
			singleton = new ServiceRegister();
		}		
		return singleton; 
	}
		
	private ServiceRegister() {
		init();
	}
	
	public void init() {
		
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
	        
	        System.out.println("Testing OWL2DLProfile...");
			OWL2DLProfile profile = new OWL2DLProfile();
			OWLProfileReport report = profile.checkOntology( ontology );
			for(OWLProfileViolation v:report.getViolations()) {
				System.out.println(v);
			}

		} catch (OWLOntologyCreationException e) {
			e.printStackTrace();
		
		}
	}
	
	public void saveOntologyAs(String parentPathName, String childpathName) {
		
		File output = new File(parentPathName, childpathName);
		IRI savedIRI = IRI.create(output);
		
		try {
			manager.saveOntology(ontology, savedIRI);
		} catch (OWLOntologyStorageException e) {
			e.printStackTrace();
			
		}
	}
	
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
	
	public void listIndividuals(){
		System.out.println();
		System.out.println("**********ListIndividuals**********");
		int individuals = ontology.getIndividualsInSignature().size();			
		System.out.println("Number of individuals : 	" + individuals);
		
		for (OWLNamedIndividual ind : ontology.getIndividualsInSignature()){				
			System.out.println( ind.getIRI() );
		}		
	}
	
	public void addServiceIndividual(String serviceName){
		
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
		
        System.out.println("Testing OWL2DLProfile...");
		OWL2DLProfile profile = new OWL2DLProfile();
		OWLProfileReport report = profile.checkOntology( ontology );
		for(OWLProfileViolation v:report.getViolations()) {
			System.out.println(v);
		}
		
	}
	
	public void addInfoMessageToServive(String serviceName, String infoMessage) {
		
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
		addDataProperty("hasValue", pHeight , 95);
		addObjectProperty("hasParameter", message, pHeight);
		
		//adding weight output parameter		
		OWLIndividual pWeight = addIndividualToOWLClass(infoMessage+"_pWeight", "OutputParameter");
		OWLIndividual weight = dataFactory.getOWLNamedIndividual( IRI.create(prefix + "weight") );
		addObjectProperty("hasType", pWeight, weight);
		addDataProperty("hasValue", pWeight , 61.8);
		addObjectProperty("hasParameter", message, pWeight);
		
	}

}
