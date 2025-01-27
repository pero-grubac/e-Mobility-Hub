package org.unibl.etf.emobility_hub.security.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.unibl.etf.emobility_hub.security.jwt.JwtAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
    @Autowired
    public UserDetailsServiceImpl userService;

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

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
                .csrf(AbstractHttpConfigurer::disable)
                .cors(Customizer.withDefaults())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/clients/register", "/auth/login", "/users/register", "/uploads/**").permitAll()
                        .requestMatchers("/rss/**").permitAll()

                        .requestMatchers(HttpMethod.GET, "/promotions/{id:[\\d]+}").permitAll()
                        .requestMatchers(HttpMethod.GET, "/promotions").hasRole("MANAGER")
                        .requestMatchers(HttpMethod.GET, "/promotions/getAll").hasRole("MANAGER")
                        .requestMatchers(HttpMethod.POST, "/promotions").hasRole("MANAGER")
                        .requestMatchers(HttpMethod.PUT, "/promotions").hasRole("MANAGER")
                        .requestMatchers(HttpMethod.DELETE, "/promotions/{id:[\\d]+}").hasRole("MANAGER")

                        .requestMatchers(HttpMethod.GET, "/announcements/{id:[\\d]+}").permitAll()
                        .requestMatchers(HttpMethod.GET, "/announcements").hasRole("MANAGER")
                        .requestMatchers(HttpMethod.GET, "/announcements/getAll").hasRole("MANAGER")
                        .requestMatchers(HttpMethod.POST, "/announcements").hasRole("MANAGER")
                        .requestMatchers(HttpMethod.PUT, "/announcements").hasRole("MANAGER")
                        .requestMatchers(HttpMethod.DELETE, "/announcements/{id:[\\d]+}").hasRole("MANAGER")

                        .requestMatchers(HttpMethod.GET, "/electric-cars/getByModel").permitAll()
                        .requestMatchers(HttpMethod.GET, "/electric-cars/{id:[\\d]+}").permitAll()
                        .requestMatchers(HttpMethod.PUT, "/electric-cars").permitAll()
                        .requestMatchers(HttpMethod.POST, "/electric-cars").permitAll()
                        .requestMatchers(HttpMethod.DELETE, "/electric-cars/{id:[\\d]+}").permitAll()

                        .requestMatchers(HttpMethod.GET, "/electric-scooters/getByModel").permitAll()
                        .requestMatchers(HttpMethod.GET, "/electric-scooters/{id:[\\d]+}").permitAll()
                        .requestMatchers(HttpMethod.PUT, "/electric-scooters").permitAll()
                        .requestMatchers(HttpMethod.POST, "/electric-scooters").permitAll()
                        .requestMatchers(HttpMethod.DELETE, "/electric-scooters/{id:[\\d]+}").permitAll()

                        .requestMatchers(HttpMethod.GET, "/electric-bicycles/getByModel").permitAll()
                        .requestMatchers(HttpMethod.GET, "/electric-bicycles/{id:[\\d]+}").permitAll()
                        .requestMatchers(HttpMethod.PUT, "/electric-bicycles").permitAll()
                        .requestMatchers(HttpMethod.POST, "/electric-bicycles").permitAll()
                        .requestMatchers(HttpMethod.DELETE, "/electric-bicycles/{id:[\\d]+}").permitAll()

                        .requestMatchers(HttpMethod.GET, "/manufacturers").permitAll()
                        .requestMatchers(HttpMethod.GET, "/manufacturers/getAll").permitAll()
                        .requestMatchers(HttpMethod.GET, "/manufacturers/getAllByName").permitAll()

                        .requestMatchers(HttpMethod.POST, "/parse-vehicle").permitAll()


                        .anyRequest().authenticated()
                )
                .authenticationProvider(daoAuthenticationProvider())
                .httpBasic(Customizer.withDefaults())
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}
