package org.unibl.etf.emobility_hub_promotions.services;

import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import org.unibl.etf.emobility_hub_promotions.models.beans.AnnouncementResponseBean;
import org.unibl.etf.emobility_hub_promotions.models.dto.PaginatedResponse;
import org.unibl.etf.emobility_hub_promotions.models.dto.request.AnnouncementRequest;
import org.unibl.etf.emobility_hub_promotions.models.dto.request.PromotionRequest;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

public class AnnouncementService {
	private static final String ANNOUCEMENT_URL = "http://localhost:8080/announcements";
	private ObjectMapper objectMapper;

	public AnnouncementService() {
		objectMapper = new ObjectMapper();
		objectMapper.registerModule(new JavaTimeModule());
	}
	
	public PaginatedResponse<AnnouncementResponseBean> getAnnoucements(String token, int page, int size, String search)throws Exception{
		String urlWithParams = ANNOUCEMENT_URL + "/getAll" + "?page=" + page + "&size=" + size + "&sort=id,desc";
		if (search != null && !search.trim().isEmpty()) {
			urlWithParams += "&search=" + URLEncoder.encode(search, "UTF-8");
		}

		URL url = new URL(urlWithParams);
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setRequestMethod("GET");
		connection.setRequestProperty("Accept", "application/json");
		connection.setRequestProperty("Authorization", "Bearer " + token);

		if (connection.getResponseCode() != 200) {
			throw new RuntimeException("Failed to fetch promotions: HTTP error code " + connection.getResponseCode());
		}

		objectMapper.registerModule(new JavaTimeModule());

		try (InputStreamReader reader = new InputStreamReader(connection.getInputStream())) {
			return objectMapper.readValue(reader, new TypeReference<PaginatedResponse<AnnouncementResponseBean>>() {
			});
		}
	}
	public void create(String token, AnnouncementRequest announcementRequest) throws Exception {
		URL url = new URL(ANNOUCEMENT_URL);
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setRequestMethod("POST");
		connection.setRequestProperty("Authorization", "Bearer " + token);
		connection.setRequestProperty("Content-Type", "application/json");
		connection.setDoOutput(true);
		String jsonRequest = objectMapper.writeValueAsString(announcementRequest);

		try (OutputStream os = connection.getOutputStream()) {
			os.write(jsonRequest.getBytes());
			os.flush();
		}

		if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
			throw new RuntimeException("Failed to create promotion. HTTP error code: " + connection.getResponseCode());
		}
	}
}
