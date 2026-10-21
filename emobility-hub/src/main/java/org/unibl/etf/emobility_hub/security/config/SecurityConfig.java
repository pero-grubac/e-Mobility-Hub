package org.unibl.etf.emobility_hub.security.config;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.unibl.etf.emobility_hub.security.jwt.JwtAuthenticationFilter;
import org.unibl.etf.emobility_hub.security.jwt.SessionCookies;

import java.util.Set;

import static org.springframework.http.HttpMethod.*;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    // Every employee of the company.
    static final String[] EMPLOYEE = {"ADMIN", "MANAGER", "OPERATOR"};
    // Employees who manage master data (fleet, manufacturers, staff).
    static final String[] BACK_OFFICE = {"ADMIN", "MANAGER"};
    static final String MANAGER = "MANAGER";

    private static final Set<String> SAFE_METHODS = Set.of("GET", "HEAD", "OPTIONS", "TRACE");

    private final UserDetailsServiceImpl userService;

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    private final SessionCookies sessionCookies;

    @Bean
    public static PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(userService);
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        return daoAuthenticationProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf
                        .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                        .csrfTokenRequestHandler(new SpaCsrfTokenRequestHandler())
                        .requireCsrfProtectionMatcher(cookieAuthenticatedWrite()))
                .cors(Customizer.withDefaults())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling(e -> e.authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)))
                .authorizeHttpRequests(auth -> auth
                        // Error dispatches run without the JWT filter; without this a 403 is reported as 401.
                        .requestMatchers("/error").permitAll()

                        // ── Public ─────────────────────────────────────────────
                        .requestMatchers(POST, "/auth/login", "/auth/logout").permitAll()
                        .requestMatchers(GET, "/actuator/health/**", "/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll()
                        .requestMatchers(GET, "/rss/**", "/uploads/**").permitAll()
                        .requestMatchers(GET, "/promotions/{id:\\d+}", "/announcements/{id:\\d+}").permitAll()
                        .requestMatchers(POST, "/clients/register").permitAll()

                        .requestMatchers(GET, "/auth/me").authenticated()

                        // ── Promotions app ─────────────────────────────────────
                        .requestMatchers("/promotions/**", "/announcements/**").hasRole(MANAGER)

                        // ── Fleet ──────────────────────────────────────────────
                        .requestMatchers(GET, "/electric-*/**").hasAnyRole(EMPLOYEE)
                        .requestMatchers(PUT, "/electric-*/rent/*", "/electric-*/brake/*", "/electric-*/fix/*").hasAnyRole(EMPLOYEE)
                        .requestMatchers("/electric-*/**", "/parse-vehicle").hasAnyRole(BACK_OFFICE)
                        .requestMatchers(GET, "/vehicles/**").hasAnyRole(EMPLOYEE)
                        .requestMatchers(PUT, "/vehicles/*/rent-price").hasRole(MANAGER)
                        .requestMatchers(GET, "/manufacturers/**").hasAnyRole(EMPLOYEE)
                        .requestMatchers("/manufacturers/**").hasAnyRole(BACK_OFFICE)

                        // ── Faults and rentals ─────────────────────────────────
                        .requestMatchers(GET, "/faults/**").hasAnyRole(EMPLOYEE)
                        .requestMatchers(POST, "/faults").hasAnyRole(EMPLOYEE)
                        .requestMatchers("/faults/**").hasAnyRole(BACK_OFFICE)
                        .requestMatchers(GET, "/rentals/**").hasAnyRole(EMPLOYEE)

                        // ── People ─────────────────────────────────────────────
                        .requestMatchers(GET, "/clients/**").hasAnyRole(EMPLOYEE)
                        .requestMatchers(PUT, "/clients/block/*").hasAnyRole(EMPLOYEE)
                        .requestMatchers("/clients/**").hasAnyRole(BACK_OFFICE)
                        .requestMatchers("/users/**").hasAnyRole(BACK_OFFICE)

                        // ── Reports ────────────────────────────────────────────
                        .requestMatchers("/statistics/**").hasRole(MANAGER)

                        .anyRequest().denyAll()
                )
                .authenticationProvider(daoAuthenticationProvider())
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    // CSRF only matters when the browser attaches the session cookie by itself.
    // Bearer-token clients and the login request itself are exempt.
    private RequestMatcher cookieAuthenticatedWrite() {
        return (HttpServletRequest request) ->
                !SAFE_METHODS.contains(request.getMethod())
                        && request.getHeader(HttpHeaders.AUTHORIZATION) == null
                        && sessionCookies.hasSessionCookie(request)
                        && !request.getRequestURI().equals(request.getContextPath() + "/auth/login");
    }
}
