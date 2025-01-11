package org.unibl.etf.emobility_hub.security.auth.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import org.unibl.etf.emobility_hub.exception.EntityNotFoundException;
import org.unibl.etf.emobility_hub.models.dto.request.AuthRequest;
import org.unibl.etf.emobility_hub.models.dto.response.AuthResponse;
import org.unibl.etf.emobility_hub.repositories.UserEntityRepository;
import org.unibl.etf.emobility_hub.security.auth.IAuthenticationService;
import org.unibl.etf.emobility_hub.security.jwt.JwtService;

@Service
public class AuthenticationServiceImpl implements IAuthenticationService {
    @Autowired
    private JwtService jwtService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserEntityRepository userEntityRepository;

    @Override
    public AuthResponse login(AuthRequest request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
        var user = userEntityRepository.getByUsername(request.getUsername()).orElseThrow(
                () -> new EntityNotFoundException("User not found")
        );
        var token = jwtService.generateToken(user);
        return AuthResponse.builder().token(token).build();
    }
}
