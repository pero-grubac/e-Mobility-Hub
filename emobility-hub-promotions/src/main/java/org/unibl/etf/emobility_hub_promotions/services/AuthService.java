package org.unibl.etf.emobility_hub_promotions.services;

import org.unibl.etf.emobility_hub_promotions.models.dto.request.AuthRequest;
import org.unibl.etf.emobility_hub_promotions.models.dto.response.AuthResponse;

public class AuthService {

	public AuthResponse login(AuthRequest request) {
		System.out.println(request);
		return new AuthResponse("Token");
	}
}
