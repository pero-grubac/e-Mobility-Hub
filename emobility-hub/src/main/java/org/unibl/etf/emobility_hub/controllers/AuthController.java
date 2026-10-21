package org.unibl.etf.emobility_hub.controllers;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.unibl.etf.emobility_hub.models.dto.request.AuthRequest;
import org.unibl.etf.emobility_hub.models.dto.response.AuthResponse;
import org.unibl.etf.emobility_hub.models.dto.response.SessionResponse;
import org.unibl.etf.emobility_hub.security.auth.IAuthenticationService;
import org.unibl.etf.emobility_hub.security.jwt.JwtService;
import org.unibl.etf.emobility_hub.security.jwt.SessionCookies;

import java.time.Duration;
import java.time.Instant;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final IAuthenticationService service;

    private final SessionCookies sessionCookies;

    private final JwtService jwtService;

    // Sets the httpOnly session cookie for browsers and also returns the token
    // for server-side clients.
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest authRequest) {
        AuthResponse authResponse = service.login(authRequest);
        Duration maxAge = Duration.between(Instant.now(), authResponse.getExpiresAt());
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, sessionCookies.create(authResponse.getToken(), maxAge).toString())
                .body(authResponse);
    }

    @GetMapping("/me")
    public ResponseEntity<SessionResponse> me(@AuthenticationPrincipal UserDetails user, HttpServletRequest request) {
        String token = sessionCookies.resolveToken(request).orElseThrow();
        String role = user.getAuthorities().iterator().next().getAuthority();
        return ResponseEntity.ok(new SessionResponse(user.getUsername(), role, jwtService.extractExpiration(token)));
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout() {
        return ResponseEntity.noContent()
                .header(HttpHeaders.SET_COOKIE, sessionCookies.clear().toString())
                .build();
    }
}
