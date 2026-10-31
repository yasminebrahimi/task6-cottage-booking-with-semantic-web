
package fi.jyu.ties4520.cottage;

import java.util.List;

import org.apache.jena.rdf.model.Resource;

public class CottageRepositoryTestRun {

    public static void main(String[] args) {

        // Create repository and load RDF data
        CottageRepository repository = new CottageRepository();

        // Retrieve all cottages
        List<Resource> cottages = repository.getAllCottages();

        System.out.println("...................");
        System.out.println("Total cottages: " + cottages.size());
        System.out.println("...................");

        // Print all cottages
        for (Resource cottage : cottages) {

            System.out.println("Cottage ID: " + cottage.getLocalName());

            System.out.println("Cottage URI: " + cottage.getURI());

            System.out.println("Cottage Name: "
                    + repository.getCottageName(cottage));

            System.out.println("...................");
        }

        // Test retrieving a cottage by ID
        System.out.println("Searching for Cottage1:");

        Resource cottage = repository.getCottageById("Cottage1");

        if (cottage != null) {

            System.out.println("Cottage ID: " + cottage.getLocalName());

            System.out.println("Cottage Name: "
                    + repository.getCottageName(cottage));

        } else {
            System.out.println("Cottage not found!");
        }
    }
}
