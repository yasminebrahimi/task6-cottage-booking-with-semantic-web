package fi.jyu.ties4520.cottage;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.ResolverStyle;
import java.util.concurrent.atomic.AtomicLong;

public class BookingSuggestion {
	
	// Unique booking number generator
	private static final AtomicLong BOOKING_COUNTER = new AtomicLong(System.currentTimeMillis());
	
	// Date format required in dd.mm.yyyy
		private static final DateTimeFormatter DATE_FORMAT = 
				DateTimeFormatter.ofPattern("dd.MM.uuuu")
				.withResolverStyle(ResolverStyle.STRICT);	
	
	// 11 output fields required by the assignment
	private final String bookerName;
	private final long bookingNumber;
	private final String cottageAddress;
	private final String imageUrl;
	private final int capacity;
	private final int bedrooms;
	private final int lakeDistance;
	private final String nearestCity;
	private final int cityDistance;
	private final LocalDate startDate;
	private final LocalDate endDate;
	
	//Constructor
	public BookingSuggestion(
			String bookerName,
			String cottageAddress,
			String imageUrl,
			int capacity,
			int bedrooms,
			int lakeDistance,
			String nearestCity,
			int cityDistance,
			LocalDate startDate,
			LocalDate endDate
			) {
		
		// Validate required fields
		if (bookerName == null || bookerName.isBlank()) {
			throw new IllegalArgumentException("Booker name is required.");
		}
		
		if (cottageAddress == null || cottageAddress.isBlank()) {
			throw new IllegalArgumentException("Cottage address is required.");
		}
		
		if (imageUrl == null || imageUrl.isBlank()) {
			throw new IllegalArgumentException("Image URL is required.");
		}
		
		if (nearestCity == null || nearestCity.isBlank()) {
            throw new IllegalArgumentException("Nearest city is required");
        }
		
		if (capacity < 1 || bedrooms < 1) {
            throw new IllegalArgumentException("Invalid cottage capacity or bedrooms");
        }
		
		if (lakeDistance < 0 || cityDistance < 0) {
            throw new IllegalArgumentException("Distances cannot be negative");
        }
		
		if (startDate == null || endDate == null || endDate.isBefore(startDate)) {
            throw new IllegalArgumentException("Invalid booking period");
        }
		
		// Generate a unique number for this suggestion
		this.bookingNumber = BOOKING_COUNTER.incrementAndGet();
		
		// Storage cottage and booking information
		this.bookerName = bookerName.trim();
		this.cottageAddress = cottageAddress.trim();
		this.imageUrl = imageUrl;
		this.capacity = capacity;
		this.bedrooms = bedrooms;
		this.lakeDistance = lakeDistance;
		this.nearestCity = nearestCity.trim();
		this.cityDistance = cityDistance;
		this.startDate = startDate;
		this.endDate = endDate;

	}
	
	// Getter methods


	public String getBookerName() {
		return bookerName;
	}

	public long getBookingNumber() {
		return bookingNumber;
	}

	public String getCottageAddress() {
		return cottageAddress;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public int getCapacity() {
		return capacity;
	}

	public int getBedrooms() {
		return bedrooms;
	}

	public int getLakeDistance() {
		return lakeDistance;
	}

	public String getNearestCity() {
		return nearestCity;
	}

	public int getCityDistance() {
		return cityDistance;
	}

	public LocalDate getStartDate() {
		return startDate;
	}

	public LocalDate getEndDate() {
		return endDate;
	}
	
	// Format booking dates for display
	public String getFormattedStartDate() {
		return startDate.format(DATE_FORMAT);
	}
	
	public String getFormattedEndDate() {
		return endDate.format(DATE_FORMAT);
	}
	
	// Print booking suggestion details
	@Override
    public String toString() {

        return "Booking Suggestion\n" +
                "----------------------------\n" +
                "Booker: " + bookerName + "\n" +
                "Booking Number: " + bookingNumber + "\n" +
                "Address: " + cottageAddress + "\n" +
                "Image URL: " + imageUrl + "\n" +
                "Capacity: " + capacity + "\n" +
                "Bedrooms: " + bedrooms + "\n" +
                "Lake Distance: " + lakeDistance + " m\n" +
                "Nearest City: " + nearestCity + "\n" +
                "City Distance: " + cityDistance + " km\n" +
                "Start Date: " + getFormattedStartDate() + "\n" +
                "End Date: " + getFormattedEndDate() + "\n";
    }
}
