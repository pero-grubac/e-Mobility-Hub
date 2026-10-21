package org.unibl.etf.emobility_hub_promotions.util;

// Escapes text before it is written into a page, so titles and content typed
// by users cannot inject markup or scripts.
public final class Html {
	private Html() {
	}

	public static String escape(String value) {
		if (value == null) {
			return "";
		}
		StringBuilder out = new StringBuilder(value.length());
		for (char c : value.toCharArray()) {
			switch (c) {
			case '&' -> out.append("&amp;");
			case '<' -> out.append("&lt;");
			case '>' -> out.append("&gt;");
			case '"' -> out.append("&quot;");
			case '\'' -> out.append("&#39;");
			default -> out.append(c);
			}
		}
		return out.toString();
	}

	// Short preview for list cards.
	public static String preview(String value, int max) {
		if (value == null) {
			return "";
		}
		return escape(value.length() > max ? value.substring(0, max) + "..." : value);
	}
}
