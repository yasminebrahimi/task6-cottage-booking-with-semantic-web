package fi.jyu.ties4520.cottage;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.riot.Lang;
import org.apache.jena.riot.RDFDataMgr;
import org.apache.jena.vocabulary.RDF;

// Load the cottage RDF data
public class CottageRepository {
	
	//Cottage ontology namespace
	private static final String NS = "http://localhost:8080/cottage-booking/onto/cottage.owl#";
	
	//Cottage data namespace
	private static final String DATA_NS = "http://localhost:8080/cottage-booking/data/cottages.ttl#";
	
	private final Model model;
	
	// Constructor: Load the cottage RDF database
	public CottageRepository() {
		
		// Create an empty model
		model = ModelFactory.createDefaultModel();
		
		try (InputStream input = getClass().getResourceAsStream("/data/cottages.ttl")) {
			if(input == null) {
				throw new IllegalStateException("Data file not found on classpath: " + DATA_NS);
			}
			
			// Read RDF data in Turtle format
			RDFDataMgr.read(model, input, Lang.TURTLE);
			
			System.out.println("RDF data loaded successfully!");
			System.out.println("Total RDF triples: " + model.size());
			
		} catch (IOException e) {
			throw new IllegalStateException("Could not read " + DATA_NS, e);
		}
	}
	
	//Get all cottages from the RDF database
	public List<Resource> getAllCottages() {
		
		Resource cottageClass = model.createResource(NS + "Cottage");
		
		return model.listResourcesWithProperty(RDF.type, cottageClass).toList();
	}
	
	// Retrieve an individual cottage by ID
	public Resource getCottageById(String cottageId) {
		
		Resource cottage = model.getResource(DATA_NS + cottageId);
		
		Resource cottageClass = model.getResource(NS + "Cottage");
		
		if (cottage.hasProperty(RDF.type, cottageClass)) {
			return cottage;
		}
		
		return null;
	}
	
	// Retrieve cottage name
    public String getCottageName(Resource cottage) {

        Property nameProperty = model.createProperty(NS + "cottageName");

        if (cottage.hasProperty(nameProperty)) {
            return cottage.getProperty(nameProperty).getString();
        }

        return "Unknown Cottage";
    }
    
    // Test the RDF connection
    public static void main(String[] args) {
    	
    	CottageRepository repository = new CottageRepository();
    	
    	List<Resource> cottages = repository.getAllCottages();
    	
    	System.out.println("...................");
    	System.out.println("Total cottages: " + cottages.size());
    	System.out.println("...................");
    	
    	for (Resource cottage: cottages) {
    		
    		System.out.println("Cottage URI: " + cottage.getURI());
    		
    		System.out.println("Cottage Name: " + repository.getCottageName(cottage));
    		
    		System.out.println("...........");
    	}
    }
}
