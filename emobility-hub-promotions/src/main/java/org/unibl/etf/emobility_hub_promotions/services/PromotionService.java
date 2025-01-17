package org.unibl.etf.emobility_hub_promotions.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.net.URLEncoder;

import org.unibl.etf.emobility_hub_promotions.models.beans.PromotionResponseBean;
import org.unibl.etf.emobility_hub_promotions.models.dto.PaginatedResponse;
import org.unibl.etf.emobility_hub_promotions.models.dto.request.PromotionRequest;

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

public class PromotionService {
	private static final String PROMOTIONS_URL = "http://localhost:8080/promotions";
	private ObjectMapper objectMapper;
	 public PromotionService() {
	        objectMapper = new ObjectMapper();
	        objectMapper.registerModule(new JavaTimeModule());
	    }
	public PaginatedResponse<PromotionResponseBean> getPromotions(String token, int page, int size, String search) throws Exception {
	    String urlWithParams = PROMOTIONS_URL+"/getAll" + "?page=" + page + "&size=" + size + "&sort=id,desc";
	    
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
			return objectMapper.readValue(reader, new TypeReference<PaginatedResponse<PromotionResponseBean>>() {
			});
		}
	}

	public PromotionResponseBean updatePromotion(String token, Long id, PromotionRequest promotionRequest)
			throws Exception {
		URL url = new URL(PROMOTIONS_URL );
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setRequestMethod("PUT");
		connection.setRequestProperty("Authorization", "Bearer " + token);
		connection.setRequestProperty("Content-Type", "application/json");
		connection.setDoOutput(true);
		
		promotionRequest.setId(id);
		DateTimeFormatter isoFormatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
	    promotionRequest.setId(id);
	    promotionRequest.setStartDate(promotionRequest.getStartDate().replace(" ", "T"));
	    promotionRequest.setEndDate(promotionRequest.getEndDate().replace(" ", "T"));
		String jsonRequest = objectMapper.writeValueAsString(promotionRequest);

		try (OutputStream os = connection.getOutputStream()) {
			os.write(jsonRequest.getBytes());
			os.flush();
		}

		if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
			return objectMapper.readValue(connection.getInputStream(), PromotionResponseBean.class);
		} else {
			throw new RuntimeException("Failed to update promotion. HTTP error code: " + connection.getResponseCode());
		}
	}

	public void deletePromotion(String token, Long id) throws Exception {
		URL url = new URL(PROMOTIONS_URL + "/" + id);
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setRequestMethod("DELETE");
		connection.setRequestProperty("Authorization", "Bearer " + token);

		if (connection.getResponseCode() != HttpURLConnection.HTTP_NO_CONTENT) {
			throw new RuntimeException("Failed to delete promotion. HTTP error code: " + connection.getResponseCode());
		}
	}
	public PromotionResponseBean getPromotionById(String token, Long id) throws Exception {
        URL url = new URL(PROMOTIONS_URL + "/" + id);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Authorization", "Bearer " + token);
        connection.setRequestProperty("Accept", "application/json");

        if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
            try (InputStreamReader reader = new InputStreamReader(connection.getInputStream())) {
                return objectMapper.readValue(reader, PromotionResponseBean.class);
            }
        } else {
            throw new RuntimeException("Failed to fetch promotion details. HTTP error code: " + connection.getResponseCode());
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
	        os.write(jsonRequest.getBytes());
	        os.flush();
	    }

	    if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
	        throw new RuntimeException("Failed to create promotion. HTTP error code: " + connection.getResponseCode());
	    }
	}

}
