package org.unibl.etf.emobility_hub_promotions.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.unibl.etf.emobility_hub_promotions.properties.AppConfig;

import java.io.OutputStream;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class AuthService implements Serializable {
	private static final long serialVersionUID = 1L;
	private final String LOGIN_URL;

	// What the API returns for a successful login.
	public static final class LoginResult implements Serializable {
		private static final long serialVersionUID = 1L;
		private final String username;
		private final String role;
		private final String token;

		LoginResult(String username, String role, String token) {
			this.username = username;
			this.role = role;
			this.token = token;
		}

		public String getUsername() {
			return username;
		}

		public String getRole() {
			return role;
		}

		public String getToken() {
			return token;
		}
	}

	public AuthService() {
		this(new AppConfig().getLoginUrl());
	}

	public AuthService(String loginUrl) {
		LOGIN_URL = loginUrl;
	}

	// Returns null when the credentials are wrong or the API cannot be reached.
	public LoginResult login(String username, String password) {
		try {
			URL url = new URL(LOGIN_URL);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type", "application/json");
			conn.setDoOutput(true);

			// Serialised by Jackson so quotes or backslashes in a password stay valid JSON.
			ObjectMapper objectMapper = new ObjectMapper();
			String jsonInputString = objectMapper.writeValueAsString(Map.of("username", username, "password", password));
			try (OutputStream os = conn.getOutputStream()) {
				byte[] input = jsonInputString.getBytes(StandardCharsets.UTF_8);
				os.write(input, 0, input.length);
			}

			int responseCode = conn.getResponseCode();
			if (responseCode == HttpURLConnection.HTTP_OK) {
				Map<?, ?> responseMap = objectMapper.readValue(conn.getInputStream(), Map.class);
				return new LoginResult((String) responseMap.get("username"), (String) responseMap.get("role"),
						(String) responseMap.get("token"));
			} else {
				throw new RuntimeException("Login failed with HTTP code: " + responseCode);
			}
		} catch (Exception e) {
			System.err.println("Login error: " + e.getMessage());
			return null;
		}
	}
}
