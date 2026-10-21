package org.unibl.etf.emobility_hub_promotions.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

// Server-side checks for the promotion and announcement forms. They mirror the
// HTML constraints and the database column sizes.
public final class FormValidation {
	public static final int TITLE_MAX = 255;
	public static final int PROMOTION_CONTENT_MAX = 1500;
	public static final int ANNOUNCEMENT_CONTENT_MAX = 255;

	// The value format of <input type="datetime-local">.
	public static final DateTimeFormatter DATE_TIME_INPUT = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");

	private FormValidation() {
	}

	public static List<String> validateAnnouncement(String title, String content) {
		List<String> errors = new ArrayList<>();
		checkText(errors, "Title", title, TITLE_MAX);
		checkText(errors, "Content", content, ANNOUNCEMENT_CONTENT_MAX);
		return errors;
	}

	public static List<String> validatePromotion(String title, String content, String startDate, String endDate) {
		List<String> errors = new ArrayList<>();
		checkText(errors, "Title", title, TITLE_MAX);
		checkText(errors, "Description", content, PROMOTION_CONTENT_MAX);
		LocalDateTime start = parseDate(errors, "Start date", startDate);
		LocalDateTime end = parseDate(errors, "End date", endDate);
		if (start != null && end != null && !end.isAfter(start)) {
			errors.add("End date must be after the start date.");
		}
		return errors;
	}

	private static void checkText(List<String> errors, String field, String value, int max) {
		if (value == null || value.trim().isEmpty()) {
			errors.add(field + " is required.");
		} else if (value.trim().length() > max) {
			errors.add(field + " can have at most " + max + " characters.");
		}
	}

	private static LocalDateTime parseDate(List<String> errors, String field, String value) {
		if (value == null || value.trim().isEmpty()) {
			errors.add(field + " is required.");
			return null;
		}
		try {
			return LocalDateTime.parse(value.trim(), DATE_TIME_INPUT);
		} catch (DateTimeParseException e) {
			errors.add(field + " is not a valid date and time.");
			return null;
		}
	}
}
