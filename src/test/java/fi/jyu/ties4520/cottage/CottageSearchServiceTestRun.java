package fi.jyu.ties4520.cottage;

import java.util.List;

public class CottageSearchServiceTestRun {

	public static void main(String[] args) {

		CottageSearchService service = new CottageSearchService(new CottageRepository());

		// Cottage1 is booked 20.12-27.12.
		// Starts 13.12-15.12 fit, 16.12 and 17.12 would run into the booking.
		run(service, "1) Cottage1, partly blocked by a booking (expect 3)",
				new BookingRequest("Anna", 4, 2, 200, "City1", 30, 5, "15.12.2026", 2));

		// Cottage7 is booked 05.11-30.11. City typed in lower case on purpose.
		// Starts 28.11-30.11 overlap the booking, 01.12-04.12 fit.
		run(service, "2) Cottage7, lower-case city name (expect 4)",
				new BookingRequest("Mikko", 2, 1, 300, "city7", 10, 3, "01.12.2026", 3));

		// Cottage3 only has 2 places.
		run(service, "3) Too many people for Cottage3 (expect 0)",
				new BookingRequest("Liisa", 4, 1, 100, "City3", 20, 3, "10.11.2026", 1));

		// Cottage4 is booked 15.12-31.12, so every start date is blocked.
		run(service, "4) Cottage4 fully booked (expect 0)",
				new BookingRequest("Pekka", 6, 3, 300, "City4", 10, 3, "20.12.2026", 2));
	}

	private static void run(CottageSearchService service, String title, BookingRequest request) {

		List<BookingSuggestion> suggestions = service.search(request);

		System.out.println("...................");
		System.out.println(title + " -> found " + suggestions.size());

		for (BookingSuggestion s : suggestions) {
			System.out.println("  #" + s.getBookingNumber() + "  " + s.getCottageAddress()
					+ "  " + s.getFormattedStartDate() + " - " + s.getFormattedEndDate()
					+ "  (" + s.getNearestCity() + ", " + s.getCityDistance() + " km)");
		}
	}
}
