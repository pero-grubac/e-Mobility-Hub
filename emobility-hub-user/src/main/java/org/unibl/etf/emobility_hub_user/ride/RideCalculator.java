package org.unibl.etf.emobility_hub_user.ride;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.LocalDateTime;

// How a finished ride is measured and charged. The vehicle's rent price is per hour.
public final class RideCalculator {
	private static final double EARTH_RADIUS_KM = 6371.0;
	// Every ride is charged for at least one minute.
	private static final long MINIMUM_SECONDS = 60;

	private RideCalculator() {
	}

	// Great-circle (haversine) distance in kilometres.
	public static double distanceKm(double startLat, double startLon, double endLat, double endLon) {
		double latDistance = Math.toRadians(endLat - startLat);
		double lonDistance = Math.toRadians(endLon - startLon);
		double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2) + Math.cos(Math.toRadians(startLat))
				* Math.cos(Math.toRadians(endLat)) * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
		return EARTH_RADIUS_KM * c;
	}

	// Billable duration in hours, never less than one minute.
	public static double billableHours(LocalDateTime start, LocalDateTime end) {
		long seconds = Math.max(MINIMUM_SECONDS, Duration.between(start, end).getSeconds());
		return seconds / 3600.0;
	}

	public static double price(double rentPricePerHour, LocalDateTime start, LocalDateTime end) {
		return round2(rentPricePerHour * billableHours(start, end));
	}

	public static double round2(double value) {
		return BigDecimal.valueOf(value).setScale(2, RoundingMode.HALF_UP).doubleValue();
	}
}
