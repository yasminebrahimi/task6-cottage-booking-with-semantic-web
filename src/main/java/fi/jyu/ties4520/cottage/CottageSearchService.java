package fi.jyu.ties4520.cottage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.apache.jena.query.ParameterizedSparqlString;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;

// Finds booking suggestions that match a booking request
public class CottageSearchService {

	// One SPARQL query does the whole search:
	// - VALUES lists every possible stay (start and end date)
	// - the triple patterns read the cottage details
	// - FILTERs check the request limits
	// - FILTER NOT EXISTS drops stays that overlap a booked period
	private static final String SEARCH_QUERY = """
			PREFIX :    <http://localhost:8080/cottage-booking/onto/cottage.owl#>
			PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>

			SELECT ?cottage ?name ?address ?image ?capacity ?bedrooms
			       ?lakeDistance ?city ?cityDistance ?start ?end
			WHERE {
				%s

				?cottage a :Cottage ;
					:cottageName ?name ;
					:hasAddress ?address ;
					:hasImage ?image ;
					:hasCapacity ?capacity ;
					:hasBedrooms ?bedrooms ;
					:distanceFromLake ?lakeDistance ;
					:nearestCity ?cityNode ;
					:distanceToCity ?cityDistance .
				?cityNode :cityName ?city .

				FILTER (?capacity >= ?reqPlaces)
				FILTER (?bedrooms >= ?reqBedrooms)
				FILTER (?lakeDistance <= ?reqLakeDistance)
				FILTER (?cityDistance <= ?reqCityDistance)
				FILTER (LCASE(STR(?city)) = LCASE(?reqCity))

				# Two periods overlap when each starts before the other ends
				FILTER NOT EXISTS {
					?cottage :hasBookedPeriod ?booked .
					?booked :periodStart ?bookedStart ;
					        :periodEnd ?bookedEnd .
					FILTER (?bookedStart <= ?end && ?bookedEnd >= ?start)
				}
			}
			ORDER BY ?start ?name
			""";

	private final CottageRepository repository;

	public CottageSearchService(CottageRepository repository) {
		this.repository = repository;
	}

	// Return all suggestions for the request (empty list if nothing fits)
	public List<BookingSuggestion> search(BookingRequest request) {

		ParameterizedSparqlString query = new ParameterizedSparqlString(
				SEARCH_QUERY.formatted(buildStayValues(request)));

		// setLiteral inserts user input safely (no SPARQL injection)
		query.setLiteral("reqPlaces", request.getRequiredPlaces());
		query.setLiteral("reqBedrooms", request.getRequiredBedrooms());
		query.setLiteral("reqLakeDistance", request.getMaxLakeDistance());
		query.setLiteral("reqCityDistance", request.getMaxCityDistance());
		query.setLiteral("reqCity", request.getCity());

		List<BookingSuggestion> suggestions = new ArrayList<>();

		try (QueryExecution execution =
				QueryExecutionFactory.create(query.asQuery(), repository.getModel())) {

			ResultSet results = execution.execSelect();

			while (results.hasNext()) {
				QuerySolution row = results.nextSolution();

				suggestions.add(new BookingSuggestion(
						request.getBookerName(),
						row.getLiteral("address").getString(),
						row.getLiteral("image").getLexicalForm(),
						row.getLiteral("capacity").getInt(),
						row.getLiteral("bedrooms").getInt(),
						row.getLiteral("lakeDistance").getInt(),
						row.getLiteral("city").getString(),
						row.getLiteral("cityDistance").getInt(),
						LocalDate.parse(row.getLiteral("start").getLexicalForm()),
						LocalDate.parse(row.getLiteral("end").getLexicalForm())));
			}
		}

		return suggestions;
	}

	// Build the VALUES block with every possible stay.
	// Example: start 15.11, shift 2, 5 days -> starts 13.11 ... 17.11.
	// The end day is included in the stay, so 5 days from 13.11 ends on 17.11.
	private String buildStayValues(BookingRequest request) {

		StringBuilder values = new StringBuilder("VALUES (?start ?end) {\n");

		LocalDate firstStart = request.getStartDate().minusDays(request.getMaxShiftDays());
		LocalDate lastStart = request.getStartDate().plusDays(request.getMaxShiftDays());

		for (LocalDate start = firstStart; !start.isAfter(lastStart); start = start.plusDays(1)) {
			LocalDate end = start.plusDays(request.getNumberOfDays() - 1);

			values.append("\t\t\t\t(\"").append(start).append("\"^^xsd:date \"")
					.append(end).append("\"^^xsd:date)\n");
		}

		return values.append("\t\t\t}").toString();
	}
}
