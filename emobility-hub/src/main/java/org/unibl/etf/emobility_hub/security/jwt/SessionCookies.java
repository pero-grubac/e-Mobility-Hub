package org.unibl.etf.emobility_hub.security.jwt;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Arrays;
import java.util.Optional;

// The browser apps keep the JWT in an httpOnly cookie; server-to-server
// clients (the promotions app) send it as a Bearer header instead.
@Component
public class SessionCookies {
    private static final String BEARER = "Bearer ";

    private final String name;
    private final boolean secure;

    public SessionCookies(@Value("${auth.cookie.name:EMH_SESSION}") String name,
                          @Value("${auth.cookie.secure:false}") boolean secure) {
        this.name = name;
        this.secure = secure;
    }

    public ResponseCookie create(String token, Duration maxAge) {
        return base(token).maxAge(maxAge).build();
    }

    public ResponseCookie clear() {
        return base("").maxAge(0).build();
    }

    public Optional<String> resolveToken(HttpServletRequest request) {
        String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (StringUtils.startsWith(header, BEARER)) {
            return Optional.of(header.substring(BEARER.length()));
        }
        return sessionCookie(request);
    }

    public boolean hasSessionCookie(HttpServletRequest request) {
        return sessionCookie(request).isPresent();
    }

    private Optional<String> sessionCookie(HttpServletRequest request) {
        if (request.getCookies() == null) {
            return Optional.empty();
        }
        return Arrays.stream(request.getCookies())
                .filter(c -> name.equals(c.getName()))
                .map(Cookie::getValue)
                .filter(StringUtils::isNotEmpty)
                .findFirst();
    }

    private ResponseCookie.ResponseCookieBuilder base(String value) {
        return ResponseCookie.from(name, value)
                .httpOnly(true)
                .secure(secure)
                .sameSite("Strict")
                .path("/");
    }
}
