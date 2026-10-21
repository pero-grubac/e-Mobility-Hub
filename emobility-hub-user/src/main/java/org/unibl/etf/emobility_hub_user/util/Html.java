package org.unibl.etf.emobility_hub_user.util;

import java.util.Locale;

// Helpers for JSP output.
public final class Html {
	private Html() {
	}

	// Escapes text for HTML content and attribute values.
	public static String escape(Object value) {
		if (value == null) {
			return "";
		}
		String text = value.toString();
		StringBuilder out = new StringBuilder(text.length());
		for (char c : text.toCharArray()) {
			switch (c) {
				case '<' -> out.append("&lt;");
				case '>' -> out.append("&gt;");
				case '&' -> out.append("&amp;");
				case '"' -> out.append("&quot;");
				case '\'' -> out.append("&#39;");
				default -> out.append(c);
			}
		}
		return out.toString();
	}

	public static String money(Double value) {
		return value == null ? "-" : String.format(Locale.US, "%.2f EUR", value);
	}

	public static String number(Double value, String unit) {
		return value == null ? "-" : String.format(Locale.US, "%.2f %s", value, unit);
	}

	// The vehicle's picture, or a neutral placeholder when it has none.
	public static String vehicleImage(String image, String contextPath) {
		return image == null || image.isEmpty() ? contextPath + "/images/vehicle-placeholder.svg" : image;
	}
}
