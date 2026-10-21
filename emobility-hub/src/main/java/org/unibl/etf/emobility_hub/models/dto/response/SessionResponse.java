package org.unibl.etf.emobility_hub.models.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.Instant;

@Data
@AllArgsConstructor
public class SessionResponse {
    private String username;
    private String role;
    private Instant expiresAt;
}
