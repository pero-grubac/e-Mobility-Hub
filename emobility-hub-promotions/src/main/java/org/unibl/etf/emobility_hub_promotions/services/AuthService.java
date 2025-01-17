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
	private static AppConfig conf = new AppConfig();
	private static final String LOGIN_URL = conf.getLoginUrl();

	public String login(String username, String password) {
		try {
			URL url = new URL(LOGIN_URL);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type", "application/json");
			conn.setDoOutput(true);

			String jsonInputString = String.format("{\"username\":\"%s\", \"password\":\"%s\"}", username, password);
			try (OutputStream os = conn.getOutputStream()) {
				byte[] input = jsonInputString.getBytes(StandardCharsets.UTF_8);
				os.write(input, 0, input.length);
			}

			int responseCode = conn.getResponseCode();
			if (responseCode == HttpURLConnection.HTTP_OK) {
				ObjectMapper objectMapper = new ObjectMapper();

				Map<String, String> responseMap = objectMapper.readValue(conn.getInputStream(), Map.class);

				return responseMap.get("token");
			} else {
				throw new RuntimeException("Login failed with HTTP code: " + responseCode);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
