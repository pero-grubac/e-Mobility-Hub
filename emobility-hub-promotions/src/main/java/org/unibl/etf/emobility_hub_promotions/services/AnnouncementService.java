package org.unibl.etf.emobility_hub_promotions.services;

import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import org.unibl.etf.emobility_hub_promotions.properties.AppConfig;
import org.unibl.etf.emobility_hub_promotions.beans.AnnouncementResponseBean;
import org.unibl.etf.emobility_hub_promotions.models.dto.PaginatedResponse;
import org.unibl.etf.emobility_hub_promotions.models.dto.request.AnnouncementRequest;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

public class AnnouncementService {
	private final String ANNOUNCEMENTS_URL;
	private ObjectMapper objectMapper;

	public AnnouncementService() {
		this(new AppConfig().getAnnouncementsUrl());
	}

	public AnnouncementService(String announcementsUrl) {
		ANNOUNCEMENTS_URL = announcementsUrl;
		objectMapper = new ObjectMapper();
		objectMapper.registerModule(new JavaTimeModule());
	}

	public PaginatedResponse<AnnouncementResponseBean> getAnnouncements(String token, int page, int size, String search)
			throws Exception {
		String urlWithParams = ANNOUNCEMENTS_URL + "/getAll" + "?page=" + page + "&size=" + size + "&sort=id,desc";
		if (search != null && !search.trim().isEmpty()) {
			urlWithParams += "&search=" + URLEncoder.encode(search.trim(), StandardCharsets.UTF_8);
		}

		URL url = new URL(urlWithParams);
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setRequestMethod("GET");
		connection.setRequestProperty("Accept", "application/json");
		connection.setRequestProperty("Authorization", "Bearer " + token);

		ApiException.expect(connection, HttpURLConnection.HTTP_OK, "Loading announcements");

		try (InputStreamReader reader = new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8)) {
			return objectMapper.readValue(reader, new TypeReference<PaginatedResponse<AnnouncementResponseBean>>() {
			});
		}
	}

	public void create(String token, AnnouncementRequest announcementRequest) throws Exception {
		URL url = new URL(ANNOUNCEMENTS_URL);
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setRequestMethod("POST");
		connection.setRequestProperty("Authorization", "Bearer " + token);
		connection.setRequestProperty("Content-Type", "application/json");
		connection.setDoOutput(true);
		String jsonRequest = objectMapper.writeValueAsString(announcementRequest);

		try (OutputStream os = connection.getOutputStream()) {
			os.write(jsonRequest.getBytes(StandardCharsets.UTF_8));
			os.flush();
		}

		ApiException.expect(connection, HttpURLConnection.HTTP_OK, "Creating the announcement");
	}

	public AnnouncementResponseBean update(String token, Long id, AnnouncementRequest announcementRequest)
			throws Exception {
		URL url = new URL(ANNOUNCEMENTS_URL);
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setRequestMethod("PUT");
		connection.setRequestProperty("Authorization", "Bearer " + token);
		connection.setRequestProperty("Content-Type", "application/json");
		connection.setDoOutput(true);

		announcementRequest.setId(id);
		String jsonRequest = objectMapper.writeValueAsString(announcementRequest);

		try (OutputStream os = connection.getOutputStream()) {
			os.write(jsonRequest.getBytes(StandardCharsets.UTF_8));
			os.flush();
		}

		ApiException.expect(connection, HttpURLConnection.HTTP_OK, "Saving the announcement");
		return objectMapper.readValue(connection.getInputStream(), AnnouncementResponseBean.class);
	}

	public void delete(String token, Long id) throws Exception {
		URL url = new URL(ANNOUNCEMENTS_URL + "/" + id);
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setRequestMethod("DELETE");
		connection.setRequestProperty("Authorization", "Bearer " + token);

		ApiException.expect(connection, HttpURLConnection.HTTP_NO_CONTENT, "Deleting the announcement");
	}

	public AnnouncementResponseBean getById(String token, Long id) throws Exception {
		URL url = new URL(ANNOUNCEMENTS_URL + "/" + id);
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setRequestMethod("GET");
		connection.setRequestProperty("Authorization", "Bearer " + token);
		connection.setRequestProperty("Accept", "application/json");

		ApiException.expect(connection, HttpURLConnection.HTTP_OK, "Loading the announcement");
		try (InputStreamReader reader = new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8)) {
			return objectMapper.readValue(reader, AnnouncementResponseBean.class);
		}
	}
}
