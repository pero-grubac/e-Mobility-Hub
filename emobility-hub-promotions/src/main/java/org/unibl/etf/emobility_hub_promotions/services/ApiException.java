package org.unibl.etf.emobility_hub_promotions.services;

import java.io.IOException;
import java.net.HttpURLConnection;

// An unexpected answer from the Spring Boot API, with its HTTP status so the
// pages can tell an expired session (401/403) from any other failure.
public class ApiException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	private final int status;

	public ApiException(String action, int status) {
		super(action + " failed (HTTP " + status + ")");
		this.status = status;
	}

	public int getStatus() {
		return status;
	}

	// The token expired or no longer belongs to a manager: sign in again.
	public boolean isSessionExpired() {
		return status == HttpURLConnection.HTTP_UNAUTHORIZED || status == HttpURLConnection.HTTP_FORBIDDEN;
	}

	static void expect(HttpURLConnection connection, int expected, String action) throws IOException {
		int status = connection.getResponseCode();
		if (status != expected) {
			throw new ApiException(action, status);
		}
	}
}
