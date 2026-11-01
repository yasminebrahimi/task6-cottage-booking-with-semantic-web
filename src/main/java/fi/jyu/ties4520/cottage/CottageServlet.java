package fi.jyu.ties4520.cottage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

// Backend endpoint: /cottage-booking/search
// Reads the 9 inputs, runs the search and answers with JSON.
@WebServlet("/search")
public class CottageServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	private final ObjectMapper json = new ObjectMapper();

	private CottageSearchService searchService;

	// Load the RDF data once, when Tomcat starts the servlet
	@Override
	public void init() {
		searchService = new CottageSearchService(new CottageRepository());
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		handle(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		handle(req, resp);
	}

	private void handle(HttpServletRequest req, HttpServletResponse resp) throws IOException {

		resp.setContentType("application/json");
		resp.setCharacterEncoding("UTF-8");

		BookingRequest bookingRequest;

		try {
			bookingRequest = new BookingRequest(
					text(req, "bookerName"),
					number(req, "requiredPlaces"),
					number(req, "requiredBedrooms"),
					number(req, "maxLakeDistance"),
					text(req, "city"),
					number(req, "maxCityDistance"),
					number(req, "numberOfDays"),
					text(req, "startDate"),
					number(req, "maxShiftDays"));

		} catch (IllegalArgumentException e) {
			// Bad input: answer 400 with a message the page can show
			resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			json.writeValue(resp.getWriter(), Map.of("error", e.getMessage()));
			return;
		}

		List<Map<String, Object>> results = new ArrayList<>();

		for (BookingSuggestion s : searchService.search(bookingRequest)) {
			results.add(toJson(s));
		}

		json.writeValue(resp.getWriter(), results);
	}

	// The 11 outputs, with dates in dd.mm.yyyy format
	private Map<String, Object> toJson(BookingSuggestion s) {

		Map<String, Object> out = new LinkedHashMap<>();
		out.put("bookerName", s.getBookerName());
		out.put("bookingNumber", s.getBookingNumber());
		out.put("address", s.getCottageAddress());
		out.put("imageUrl", s.getImageUrl());
		out.put("capacity", s.getCapacity());
		out.put("bedrooms", s.getBedrooms());
		out.put("lakeDistance", s.getLakeDistance());
		out.put("nearestCity", s.getNearestCity());
		out.put("cityDistance", s.getCityDistance());
		out.put("startDate", s.getFormattedStartDate());
		out.put("endDate", s.getFormattedEndDate());
		return out;
	}

	// Read a text input; BookingRequest checks that it is not blank
	private String text(HttpServletRequest req, String name) {
		return req.getParameter(name);
	}

	// Read a whole-number input, with a clear message if it is missing or not a number
	private int number(HttpServletRequest req, String name) {

		String value = req.getParameter(name);

		if (value == null || value.isBlank()) {
			throw new IllegalArgumentException(name + " is required");
		}

		try {
			return Integer.parseInt(value.trim());
		} catch (NumberFormatException e) {
			throw new IllegalArgumentException(name + " must be a whole number");
		}
	}
}
