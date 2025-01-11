package org.unibl.etf.emobility_hub.security.auth;


import org.unibl.etf.emobility_hub.models.dto.request.AuthRequest;
import org.unibl.etf.emobility_hub.models.dto.response.AuthResponse;

public interface IAuthenticationService {
    AuthResponse login(AuthRequest request);
}
