package finki.ukim.backend.auth_and_access.config;

import finki.ukim.backend.auth_and_access.web.filter.JwtFilter;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@AllArgsConstructor
public class JwtWebSecurityConfig {
    private final JwtFilter jwtFilter;

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.setAllowedOrigins(List.of("http://localhost:3000"));
        corsConfiguration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "PATCH"));
        corsConfiguration.setAllowedHeaders(List.of("*"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration);
        return source;
    }


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(corsCustomizer ->
                        corsCustomizer.configurationSource(corsConfigurationSource())
                )

                .headers(headers -> headers
                        .frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin)
                )
//                .exceptionHandling(ex -> ex
//                        .authenticationEntryPoint((request, response, authException) ->
//                                response.sendError(HttpServletResponse.SC_UNAUTHORIZED))
//                        .accessDeniedHandler((request, response, accessDeniedException) ->
//                                response.sendError(HttpServletResponse.SC_FORBIDDEN))
//                )
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/swagger-ui/**",
                                "/v3/api-docs/**",
                                "/api/auth/**",
                                "/h2-console/**"
                        ).permitAll()
//
//                        // Administration
//                        .requestMatchers("/api/categories", "/api/categories/**").hasRole("ADMINISTRATOR")
//                        .requestMatchers("/api/departments", "/api/departments/**").hasRole("ADMINISTRATOR")
//                        .requestMatchers("/api/municipalities", "/api/municipalities/**").hasRole("ADMINISTRATOR")
//                        .requestMatchers(HttpMethod.GET, "/api/staff/by-department/**").hasRole("ADMINISTRATOR")
//                        .requestMatchers(HttpMethod.GET, "/api/staff/by-municipality/**").hasRole("ADMINISTRATOR")
//                        .requestMatchers(HttpMethod.PUT, "/api/staff/{id}").hasRole("ADMINISTRATOR")
//                        .requestMatchers("/api/staff", "/api/staff/**").hasAnyRole("ADMINISTRATOR", "MANAGER")
//
//                        // Auth and access
//                        .requestMatchers("/api/users/me/**", "/api/users/me").authenticated()
//                        .requestMatchers(
//                                HttpMethod.GET,
//                                "/api/users",
//                                "/api/users/paged",
//                                "/api/users/*"
//                        ).hasRole("ADMINISTRATOR")
//                        .requestMatchers(
//                                HttpMethod.DELETE,
//                                "/api/users/*",
//                                "/api/users/bulk"
//                        ).hasRole("ADMINISTRATOR")
//                        .requestMatchers(
//                                HttpMethod.PATCH,
//                                "/api/users/*",
//                                "/api/users/*/role",
//                                "/api/users/*/lock",
//                                "/api/users/*/unlock"
//                        ).hasRole("ADMINISTRATOR")
//                        .requestMatchers("/api/users/**").authenticated()
//
//                        // Reports
//                        .requestMatchers("/api/reports", "/api/reports/**").authenticated()
//
//                        // Request management: narrow paths first
//                        .requestMatchers(
//                                "/api/requests/{requestId}/ai-suggestion",
//                                "/api/requests/{requestId}/ai-suggestion/**"
//                        ).hasAnyRole("ADMINISTRATOR", "MANAGER")
//                        .requestMatchers(
//                                HttpMethod.GET,
//                                "/api/requests/{requestId}/assignments",
//                                "/api/requests/{requestId}/assignments/**"
//                        ).authenticated()
//                        .requestMatchers(
//                                "/api/requests/{requestId}/assignments",
//                                "/api/requests/{requestId}/assignments/**"
//                        ).hasAnyRole("ADMINISTRATOR", "MANAGER")
//                        .requestMatchers("/api/request-assignments", "/api/request-assignments/**")
//                        .hasAnyRole("ADMINISTRATOR", "MANAGER", "EMPLOYEE")
//                        .requestMatchers(
//                                "/api/requests/{requestId}/comments",
//                                "/api/requests/{requestId}/comments/**"
//                        ).authenticated()
//                        .requestMatchers(
//                                "/api/requests/{requestId}/logs",
//                                "/api/requests/{requestId}/logs/**"
//                        ).authenticated()
//                        .requestMatchers(
//                                "/api/requests/{requestId}/routing",
//                                "/api/requests/{requestId}/routing/**"
//                        ).hasAnyRole("ADMINISTRATOR", "MANAGER")
//                        .requestMatchers(HttpMethod.PATCH, "/api/requests/{id}/cancel").hasAnyRole("CITIZEN", "ADMINISTRATOR")
//                        .requestMatchers(HttpMethod.PATCH, "/api/requests/{id}/status")
//                        .hasAnyRole("ADMINISTRATOR", "MANAGER", "EMPLOYEE")
//                        .requestMatchers(HttpMethod.DELETE, "/api/requests/{id}").hasRole("ADMINISTRATOR")
//                        .requestMatchers(HttpMethod.DELETE, "/api/requests/bulk").hasRole("ADMINISTRATOR")
//                        .requestMatchers("/api/requests", "/api/requests/**").authenticated()

                        .anyRequest().authenticated()
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
