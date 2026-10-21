package org.unibl.etf.emobility_hub_user.validation;

import java.time.YearMonth;
import java.util.regex.Pattern;

// Server-side checks for every form. The pages run the same rules in the
// browser (see js/app.js and the HTML pattern attributes) before submitting.
public final class Validators {
	public static final String USERNAME_PATTERN = "[A-Za-z0-9_.]{3,30}";
	public static final String PHONE_PATTERN = "\\+?[0-9][0-9 \\-]{5,19}";
	public static final String ID_CARD_PATTERN = "[A-Za-z0-9\\-]{5,20}";
	public static final String DOCUMENT_PATTERN = "[A-Za-z0-9\\-]{5,30}";
	public static final int PASSWORD_MIN_LENGTH = 8;

	private static final Pattern USERNAME = Pattern.compile(USERNAME_PATTERN);
	private static final Pattern PHONE = Pattern.compile(PHONE_PATTERN);
	private static final Pattern ID_CARD = Pattern.compile(ID_CARD_PATTERN);
	private static final Pattern DOCUMENT = Pattern.compile(DOCUMENT_PATTERN);
	private static final Pattern EMAIL = Pattern.compile("[^@\\s]+@[^@\\s]+\\.[^@\\s]+");
	private static final Pattern EXPIRY = Pattern.compile("(0[1-9]|1[0-2])/([0-9]{2})");

	private Validators() {
	}

	public static boolean isBlank(String value) {
		return value == null || value.trim().isEmpty();
	}

	public static boolean isValidUsername(String value) {
		return value != null && USERNAME.matcher(value).matches();
	}

	public static boolean isValidEmail(String value) {
		return value != null && value.length() <= 255 && EMAIL.matcher(value).matches();
	}

	public static boolean isValidPhone(String value) {
		return value != null && PHONE.matcher(value).matches();
	}

	public static boolean isValidIdCard(String value) {
		return value != null && ID_CARD.matcher(value).matches();
	}

	public static boolean isValidDocumentNumber(String value) {
		return value != null && DOCUMENT.matcher(value).matches();
	}

	public static boolean isValidPassword(String value) {
		return value != null && value.length() >= PASSWORD_MIN_LENGTH && value.length() <= 72;
	}

	public static boolean isValidLatitude(Double value) {
		return value != null && !value.isNaN() && value >= -90 && value <= 90;
	}

	public static boolean isValidLongitude(Double value) {
		return value != null && !value.isNaN() && value >= -180 && value <= 180;
	}

	public static Double parseDouble(String value) {
		if (isBlank(value)) {
			return null;
		}
		try {
			return Double.valueOf(value.trim());
		} catch (NumberFormatException e) {
			return null;
		}
	}

	// Card numbers may be typed with spaces or dashes; only digits count.
	public static String digitsOnly(String value) {
		return value == null ? "" : value.replaceAll("[\\s\\-]", "");
	}

	// 13 to 19 digits that pass the Luhn checksum.
	public static boolean isValidCardNumber(String value) {
		String digits = digitsOnly(value);
		if (!digits.matches("[0-9]{13,19}")) {
			return false;
		}
		int sum = 0;
		boolean doubleIt = false;
		for (int i = digits.length() - 1; i >= 0; i--) {
			int d = digits.charAt(i) - '0';
			if (doubleIt) {
				d *= 2;
				if (d > 9) {
					d -= 9;
				}
			}
			sum += d;
			doubleIt = !doubleIt;
		}
		return sum % 10 == 0;
	}

	// MM/YY, valid until the end of that month.
	public static boolean isValidExpiry(String value, YearMonth now) {
		if (value == null) {
			return false;
		}
		java.util.regex.Matcher m = EXPIRY.matcher(value.trim());
		if (!m.matches()) {
			return false;
		}
		YearMonth expiry = YearMonth.of(2000 + Integer.parseInt(m.group(2)), Integer.parseInt(m.group(1)));
		return !expiry.isBefore(now);
	}

	public static boolean isValidCvv(String value) {
		return value != null && value.matches("[0-9]{3,4}");
	}

	public static String lastFour(String cardNumber) {
		String digits = digitsOnly(cardNumber);
		return digits.substring(digits.length() - 4);
	}
}
