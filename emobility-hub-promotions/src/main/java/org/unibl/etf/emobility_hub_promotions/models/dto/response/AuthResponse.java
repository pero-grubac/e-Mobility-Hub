package org.unibl.etf.emobility_hub_promotions.models.dto.response;

import java.io.Serializable;

public class AuthResponse implements Serializable {
	private static final long serialVersionUID = 1L;
	private String token;

	public AuthResponse() {
		super();
		// TODO Auto-generated constructor stub
	}

	public AuthResponse(String token) {
		super();
		this.token = token;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

}