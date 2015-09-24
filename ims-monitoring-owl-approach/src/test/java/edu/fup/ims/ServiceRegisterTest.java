package edu.fup.ims;

import java.io.File;
import java.util.Collections;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.AddAxiom;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLIndividual;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;
import org.semanticweb.owlapi.model.SWRLClassAtom;
import org.semanticweb.owlapi.model.SWRLRule;
import org.semanticweb.owlapi.model.SWRLVariable;
import org.semanticweb.owlapi.profiles.OWL2DLProfile;
import org.semanticweb.owlapi.profiles.OWLProfileReport;
import org.semanticweb.owlapi.profiles.OWLProfileViolation;
import org.semanticweb.owlapi.vocab.PrefixOWLOntologyFormat;

/**
 * 
 * @author Luis Eraso
 *
 */
public class ServiceRegisterTest {
	
	static final String soa_uri = "http://www.semanticweb.org/ontologies/2010/01/core-soa.owl";
	static final String soa_local_url = "src/main/resources/edu/fup/ims/ontologies/soa.owl";
	//static final String NS = soa_uri + "#";	
	
	public static void main(String[] args) {
		
		OWLOntologyManager m = OWLManager.createOWLOntologyManager();
		
    	File fileOntology = new File( soa_local_url );
    	IRI soaIRI = IRI.create( fileOntology );
    	
		try {
			
			// Load the ontology
			OWLOntology ontology = m.loadOntologyFromOntologyDocument( soaIRI );

			// Report information about the ontology
			System.out.println("Ontology Loaded...");
	        System.out.println("Document IRI:	" + soaIRI);
	        System.out.println("Ontology    :	" + ontology.getOntologyID());
	        System.out.println("Format      :	" + m.getOntologyFormat(ontology));
			
	        listIndividuals(ontology);
	        
	        addVitalSingService(m,ontology);
	        
	        listIndividuals(ontology);
	        
	        
			File output = new File("src/main/resources/edu/fup/ims/ontologies/", "saved_soa.owl");
			IRI savedSoaIRI = IRI.create(output);
			// save in OWL/XML format
			//m.saveOntology(ontology, new OWLXMLOntologyFormat(), savedSoaIRI);			
			// save in RDF/XML
			m.saveOntology(ontology, savedSoaIRI);
			
			
	        
			//*************Not important code
			OWL2DLProfile profile = new OWL2DLProfile();
			OWLProfileReport report = profile.checkOntology( ontology );
			for(OWLProfileViolation v:report.getViolations()) {
				System.out.println(v);
			}
			//*************Not important code
			
		} catch (OWLOntologyCreationException e) {
			e.printStackTrace();
		} catch (OWLOntologyStorageException e) {
			e.printStackTrace();
		}
		
	}
	
	public static void listIndividuals(OWLOntology ontology){
		System.out.println();
		System.out.println("**********ListIndividuals**********");
		int individuals = ontology.getIndividualsInSignature().size();			
		System.out.println("Individuals : 	" + individuals);
		
		for (OWLNamedIndividual ind : ontology.getIndividualsInSignature()){				
			System.out.println( ind.getIRI() );
		}		
	}
	
	
	public static void addVitalSingService(OWLOntologyManager m, OWLOntology ontology){
		
		System.out.println();
		System.out.println("**********AddVitalSingsService**********");
		OWLDataFactory df = OWLManager.getOWLDataFactory();
		
        // Get default namespace
		PrefixOWLOntologyFormat pm = (PrefixOWLOntologyFormat) m.getOntologyFormat(ontology);
		String ns = pm.getDefaultPrefix();
		System.out.println("namesapce   :	"+ns);
		
		//Get reference to classes
		OWLClass service = df.getOWLClass(IRI.create(ns + "Service") );
		OWLClass serviceContract = df.getOWLClass(IRI.create(ns + "ServiceContract") );
		OWLClass serviceInterface = df.getOWLClass(IRI.create(ns + "ServiceInterface") );
		OWLClass informationType = df.getOWLClass(IRI.create(ns + "InformationType") );
		OWLClass event = df.getOWLClass(IRI.create(ns + "Event") );	
		
		//1. Register vitalSigns service
		//1.1 Create individuals
		OWLIndividual vitalSings = df.getOWLNamedIndividual( IRI.create(ns + "vitalSings") );
		OWLIndividual sc1 = df.getOWLNamedIndividual( IRI.create(ns + "sc1") );
		OWLIndividual si1 = df.getOWLNamedIndividual( IRI.create(ns + "si1") );
		OWLIndividual userId = df.getOWLNamedIndividual( IRI.create(ns + "userId") );
		OWLIndividual age = df.getOWLNamedIndividual( IRI.create(ns + "age") );
		OWLIndividual weight = df.getOWLNamedIndividual( IRI.create(ns + "weight") );
		OWLIndividual height = df.getOWLNamedIndividual( IRI.create(ns + "height") );
		
		//1.2 Assign class type to individuals
		OWLAxiom a1 = df.getOWLClassAssertionAxiom(service, vitalSings);
		OWLAxiom a2 = df.getOWLClassAssertionAxiom(serviceContract, sc1);
		OWLAxiom a3 = df.getOWLClassAssertionAxiom(serviceInterface, si1);
		OWLAxiom a4 = df.getOWLClassAssertionAxiom(informationType, userId);
		OWLAxiom a5 = df.getOWLClassAssertionAxiom(informationType, age);
		OWLAxiom a6 = df.getOWLClassAssertionAxiom(informationType, weight);
		OWLAxiom a7 = df.getOWLClassAssertionAxiom(informationType, height);
		
		//1.3 Get reference to properties
		OWLObjectProperty hasContract = df.getOWLObjectProperty(IRI.create(ns + "hasContract"));
		OWLObjectProperty hasInterface = df.getOWLObjectProperty(IRI.create(ns + "hasInterface"));
		OWLObjectProperty hasInput = df.getOWLObjectProperty(IRI.create(ns + "hasInput"));
		OWLObjectProperty hasOutput = df.getOWLObjectProperty(IRI.create(ns + "hasOutput"));
		
		//1.4 Create object properties for individuals
		OWLAxiom a8 = df.getOWLObjectPropertyAssertionAxiom(hasContract, vitalSings, sc1);
		OWLAxiom a9 = df.getOWLObjectPropertyAssertionAxiom(hasInterface, vitalSings, si1);
		OWLAxiom a10 = df.getOWLObjectPropertyAssertionAxiom(hasInput, si1, userId);
		OWLAxiom a11 = df.getOWLObjectPropertyAssertionAxiom(hasOutput, si1, age);
		OWLAxiom a12 = df.getOWLObjectPropertyAssertionAxiom(hasOutput, si1, weight);
		OWLAxiom a13 = df.getOWLObjectPropertyAssertionAxiom(hasOutput, si1, height);
				
		//1.5 Finally add the axioms to the ontology
		m.applyChange(new AddAxiom(ontology, a1));
		m.applyChange(new AddAxiom(ontology, a2));
		m.applyChange(new AddAxiom(ontology, a3));
		m.applyChange(new AddAxiom(ontology, a4));
		m.applyChange(new AddAxiom(ontology, a5));
		m.applyChange(new AddAxiom(ontology, a6));
		m.applyChange(new AddAxiom(ontology, a7));
		m.applyChange(new AddAxiom(ontology, a8));
		m.applyChange(new AddAxiom(ontology, a9));
		m.applyChange(new AddAxiom(ontology, a10));
		m.applyChange(new AddAxiom(ontology, a11));
		m.applyChange(new AddAxiom(ontology, a12));
		m.applyChange(new AddAxiom(ontology, a13));
		
		
		
		//Add InformationEvent
		OWLClass information = df.getOWLClass(IRI.create(ns + "Information") );
		OWLIndividual infoEvent = df.getOWLNamedIndividual( IRI.create(ns + "infoEvent") );
		OWLAxiom ax1 = df.getOWLClassAssertionAxiom(information, infoEvent);
		m.applyChange(new AddAxiom(ontology, ax1));
		
		OWLObjectProperty generates = df.getOWLObjectProperty(IRI.create(ns + "generates"));
		OWLAxiom ax2 = df.getOWLObjectPropertyAssertionAxiom(generates, vitalSings, infoEvent);
		m.applyChange(new AddAxiom(ontology, ax2));
		
		
		/* **************************************************************************
		 * Add some restrictions and rules 
		 * 
		 */
		
		//Restriction: If an Individual has a ServiceContract this individual is a Service		
//		OWLClassExpression hasContractSomeContract = df.getOWLObjectSomeValuesFrom(hasContract, serviceContract);		
//		OWLSubClassOfAxiom ax = df.getOWLSubClassOfAxiom(service, hasContractSomeContract);
//		m.applyChange(new AddAxiom(ontology, ax));
		
		
		//Rule: "Service(?x) -> Event(?x)"		
		SWRLVariable var = df.getSWRLVariable(IRI.create(ns + "x"));		
		SWRLClassAtom body = df.getSWRLClassAtom(service, var);
		SWRLClassAtom head = df.getSWRLClassAtom(event, var);		
		SWRLRule rule = df.getSWRLRule(Collections.singleton(body),
		Collections.singleton(head));		
		m.applyChange(new AddAxiom(ontology, rule));
					
				
	}

}
