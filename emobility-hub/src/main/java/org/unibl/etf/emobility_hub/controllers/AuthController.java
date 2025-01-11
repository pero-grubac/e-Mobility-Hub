package org.unibl.etf.emobility_hub.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.unibl.etf.emobility_hub.models.dto.request.AuthRequest;
import org.unibl.etf.emobility_hub.models.dto.response.AuthResponse;
import org.unibl.etf.emobility_hub.security.auth.IAuthenticationService;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private IAuthenticationService service;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody  AuthRequest authRequest) {
        AuthResponse authResponse = service.login(authRequest);
        return ResponseEntity.ok(authResponse);
    }
}
