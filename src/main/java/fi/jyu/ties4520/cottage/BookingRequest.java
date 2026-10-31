package fi.jyu.ties4520.cottage;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;

public class BookingRequest {
	
	// Date format required in dd.mm.yyyy
	private static final DateTimeFormatter DATE_FORMAT = 
			DateTimeFormatter.ofPattern("dd.MM.uuuu")
			.withResolverStyle(ResolverStyle.STRICT);
	
	
	// Nine booking search inputs
	private final String bookerName;
	private final int requiredPlaces;
	private final int requiredBedrooms;
	private final int maxLakeDistance;
	private final String city;
	private final int maxCityDistance;
	private final int numberOfDays;
	private final LocalDate startDate;
	private final int maxShiftDays;
	
	// Constructor
	public BookingRequest(
			String bookerName,
			int requiredPlaces,
			int requiredBedrooms,
			int maxLakeDistance,
			String city,
			int maxCityDistance,
			int numberOfDays,
			String startDate,
			int maxShiftDays
			) {
		
		// Validate the input values
		if (bookerName == null || bookerName.isBlank()) {
			throw new IllegalArgumentException(
					"Booker name is required");
		}
		
		if (requiredPlaces < 1 || requiredBedrooms < 1) {
			throw new IllegalArgumentException(
					"People and bedrooms must be at least 1");
		}
		
		if (maxLakeDistance < 0 || maxCityDistance < 0) {
            throw new IllegalArgumentException(
                    "Distances cannot be negative");
        }

        if (city == null || city.isBlank()) {
            throw new IllegalArgumentException(
                    "City is required");
        }

        if (numberOfDays < 1) {
            throw new IllegalArgumentException(
                    "Number of days must be at least 1");
        }

        if (maxShiftDays < 0) {
            throw new IllegalArgumentException(
                    "Date shift cannot be negative");
        }
        
        // Store the validated inputs
        this.bookerName = bookerName.trim();
        this.requiredPlaces = requiredPlaces;
        this.requiredBedrooms = requiredBedrooms;
        this.maxLakeDistance = maxLakeDistance;
        this.city = city.trim();
        this.maxCityDistance = maxCityDistance;
        this.numberOfDays = numberOfDays;
        this.maxShiftDays = maxShiftDays;
        
        // Convert the input date (From String to LocalDate)
        try {
        	this.startDate = LocalDate.parse(startDate, DATE_FORMAT);
        } catch (DateTimeParseException e) {
        	throw new IllegalArgumentException("Start date must be in dd.mm.yyyy format", e);
        }
	}

	// Getter methods
	
	public String getBookerName() {
		return bookerName;
	}

	public int getRequiredPlaces() {
		return requiredPlaces;
	}

	public int getRequiredBedrooms() {
		return requiredBedrooms;
	}

	public int getMaxLakeDistance() {
		return maxLakeDistance;
	}

	public String getCity() {
		return city;
	}

	public int getMaxCityDistance() {
		return maxCityDistance;
	}

	public int getNumberOfDays() {
		return numberOfDays;
	}

	public LocalDate getStartDate() {
		return startDate;
	}

	public int getMaxShiftDays() {
		return maxShiftDays;
	}
	
	// Get start date in assignment format
	public String getFormattedStartDate() {
		return startDate.format(DATE_FORMAT);
	}
	
	// Display booking request information
    @Override
    public String toString() {
        return "Booker: " + bookerName +
                "\nRequired people: " + requiredPlaces +
                "\nBedrooms: " + requiredBedrooms +
                "\nMaximum lake distance: " + maxLakeDistance +
                "\nCity: " + city +
                "\nMaximum city distance: " + maxCityDistance +
                "\nDuration: " + numberOfDays +
                "\nStart date: " + getFormattedStartDate() +
                "\nShift: +/-" + maxShiftDays + " days";
    }
}
