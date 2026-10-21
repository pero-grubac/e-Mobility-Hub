package org.unibl.etf.emobility_hub_promotions.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import org.unibl.etf.emobility_hub_promotions.properties.AppConfig;
import org.unibl.etf.emobility_hub_promotions.beans.PromotionResponseBean;
import org.unibl.etf.emobility_hub_promotions.models.dto.PaginatedResponse;
import org.unibl.etf.emobility_hub_promotions.models.dto.request.PromotionRequest;

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

public class PromotionService {
	private final String PROMOTIONS_URL;
	private ObjectMapper objectMapper;

	public PromotionService() {
		this(new AppConfig().getPromotionsUrl());
	}

	public PromotionService(String promotionsUrl) {
		PROMOTIONS_URL = promotionsUrl;
		objectMapper = new ObjectMapper();
		objectMapper.registerModule(new JavaTimeModule());
	}

	public PaginatedResponse<PromotionResponseBean> getPromotions(String token, int page, int size, String search)
			throws Exception {
		String urlWithParams = PROMOTIONS_URL + "/getAll" + "?page=" + page + "&size=" + size + "&sort=id,desc";

		if (search != null && !search.trim().isEmpty()) {
			urlWithParams += "&search=" + URLEncoder.encode(search.trim(), StandardCharsets.UTF_8);
		}

		URL url = new URL(urlWithParams);
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setRequestMethod("GET");
		connection.setRequestProperty("Accept", "application/json");
		connection.setRequestProperty("Authorization", "Bearer " + token);

		ApiException.expect(connection, HttpURLConnection.HTTP_OK, "Loading promotions");

		try (InputStreamReader reader = new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8)) {
			return objectMapper.readValue(reader, new TypeReference<PaginatedResponse<PromotionResponseBean>>() {
			});
		}
	}

	public PromotionResponseBean updatePromotion(String token, Long id, PromotionRequest promotionRequest)
			throws Exception {
		URL url = new URL(PROMOTIONS_URL);
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setRequestMethod("PUT");
		connection.setRequestProperty("Authorization", "Bearer " + token);
		connection.setRequestProperty("Content-Type", "application/json");
		connection.setDoOutput(true);

		promotionRequest.setId(id);
		promotionRequest.setStartDate(promotionRequest.getStartDate().replace(" ", "T"));
		promotionRequest.setEndDate(promotionRequest.getEndDate().replace(" ", "T"));
		String jsonRequest = objectMapper.writeValueAsString(promotionRequest);

		try (OutputStream os = connection.getOutputStream()) {
			os.write(jsonRequest.getBytes(StandardCharsets.UTF_8));
			os.flush();
		}

		ApiException.expect(connection, HttpURLConnection.HTTP_OK, "Saving the promotion");
		return objectMapper.readValue(connection.getInputStream(), PromotionResponseBean.class);
	}

	public void deletePromotion(String token, Long id) throws Exception {
		URL url = new URL(PROMOTIONS_URL + "/" + id);
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setRequestMethod("DELETE");
		connection.setRequestProperty("Authorization", "Bearer " + token);

		ApiException.expect(connection, HttpURLConnection.HTTP_NO_CONTENT, "Deleting the promotion");
	}

	public PromotionResponseBean getPromotionById(String token, Long id) throws Exception {
		URL url = new URL(PROMOTIONS_URL + "/" + id);
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setRequestMethod("GET");
		connection.setRequestProperty("Authorization", "Bearer " + token);
		connection.setRequestProperty("Accept", "application/json");

		ApiException.expect(connection, HttpURLConnection.HTTP_OK, "Loading the promotion");
		try (InputStreamReader reader = new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8)) {
			return objectMapper.readValue(reader, PromotionResponseBean.class);
		}
	}

	public void createPromotion(String token, PromotionRequest promotionRequest) throws Exception {
		URL url = new URL(PROMOTIONS_URL);
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setRequestMethod("POST");
		connection.setRequestProperty("Authorization", "Bearer " + token);
		connection.setRequestProperty("Content-Type", "application/json");
		connection.setDoOutput(true);
		promotionRequest.setStartDate(promotionRequest.getStartDate().replace(" ", "T"));
		promotionRequest.setEndDate(promotionRequest.getEndDate().replace(" ", "T"));
		String jsonRequest = objectMapper.writeValueAsString(promotionRequest);

		try (OutputStream os = connection.getOutputStream()) {
			os.write(jsonRequest.getBytes(StandardCharsets.UTF_8));
			os.flush();
		}

		ApiException.expect(connection, HttpURLConnection.HTTP_OK, "Creating the promotion");
	}

}
