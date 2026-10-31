package fi.jyu.ties4520.cottage;

import java.time.LocalDate;

public class BookingSuggestionTestRun {
	
	public static void main(String[] args) {
		
		System.out.println("First Suggestion" + "\n");
		
		// Example booking suggestion
		BookingSuggestion suggestion = new BookingSuggestion(
				"Anna",
				"Address2",
				"https://example.com/cottage2.jpg",
				6,
				3,
				120,
				"City2",
				18,
				LocalDate.of(2026, 11, 15),
				LocalDate.of(2026, 11, 20)
				);
		
		System.out.println(suggestion);
		
		System.out.println("First booking number: " +
                suggestion.getBookingNumber() +"\n");
		
		System.out.println("Second Suggestion" + "\n");
		
		// Example booking suggestion 2
        BookingSuggestion suggestion2 = new BookingSuggestion(
                "Anna",
                "Address3",
                "https://example.com/cottage3.jpg",
                2,
                1,
                10,
                "City3",
                12,
                LocalDate.of(2026, 11, 13),
                LocalDate.of(2026, 11, 17)
        		);
        
        System.out.println(suggestion2);

        System.out.println("Second booking number: " +
                suggestion2.getBookingNumber());
    }
}
