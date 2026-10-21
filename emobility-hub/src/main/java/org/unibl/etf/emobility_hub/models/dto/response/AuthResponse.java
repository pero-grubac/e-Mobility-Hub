package org.unibl.etf.emobility_hub.models.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;

@Data
@Builder
public class AuthResponse {
    // Kept for server-side clients that send it as a Bearer header.
    private String token;
    private String username;
    private String role;
    private Instant expiresAt;
}
