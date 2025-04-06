package com.hask.hasktask.config;

import com.hask.hasktask.customException.CustomAccessDeniedHandler;
import com.hask.hasktask.customException.CustomAuthenticationEntryPoint;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;

import static com.hask.hasktask.config.Permission.*;
import static com.hask.hasktask.config.Role.USER;
import static org.springframework.http.HttpMethod.*;

/*
 * Interceptor: Intercept Incoming HTTP Client Request*/
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final AuthenticationProvider authenticationProvider;
    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;
    private final LogoutHandler logoutHandler;
    private static final String basePath = "/api/v1";

    // Unsecured Endpoints
    private static final String[] unSecuredEndpoint = {
            basePath + "/auth/**",
            "/caches/**",
            "/swagger-ui/**",
            // API Docs: Swagger Ui
            "/swagger-ui.html",
            "/v3/api-docs",
            "/v3/api-docs/**",
            "/swagger-resources",
            "/swagger-resources/**",
            "/configuration/ui",
            "/configuration/ui/**",
            "/swagger-resources/security",
            "/webjars/**",
            // Actuator Endpoints
            "/actuator/**",
    };

    // JWT Token Required for Accessing these Endpoints
    String[] securedEndpoints = {
            basePath + "/users/**",
            basePath + "/events/**",
            basePath + "/task/**",
            basePath + "/timers/**",
    };


    // Responsible for configuring All HTTP Security of This Application
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .cors(cor -> {
                    try {
                        cor.init(httpSecurity);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                });

        httpSecurity
                .csrf(AbstractHttpConfigurer::disable);

        httpSecurity
                .authorizeHttpRequests(auth -> {
                    // Unrestricted Endpoints (Login, Signup)
                    auth.requestMatchers(unSecuredEndpoint).permitAll();

                    // Secured Endpoints Configuration
                    for (String endpoint : securedEndpoints) {
                        auth.requestMatchers(endpoint).hasAnyRole(USER.name())
                                .requestMatchers(GET, endpoint).hasAnyAuthority(USER_READ.name())
                                .requestMatchers(POST, endpoint).hasAnyAuthority(USER_CREATE.name())
                                .requestMatchers(PUT, endpoint).hasAnyAuthority(USER_UPDATE.name())
                                .requestMatchers(PATCH, endpoint).hasAnyAuthority(USER_UPDATE.name())
                                .requestMatchers(DELETE, endpoint).hasAnyAuthority(USER_DELETE.name());
                    }

                    auth.anyRequest().authenticated();
                });

        httpSecurity
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        httpSecurity
                .authenticationProvider(authenticationProvider);

        httpSecurity
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        httpSecurity
                .logout(logoutConfigurer -> {
                    logoutConfigurer.addLogoutHandler(logoutHandler);
                    logoutConfigurer.logoutUrl("/api/v1/auth/logout").invalidateHttpSession(true);
                    logoutConfigurer.addLogoutHandler(logoutHandler);
                    // After Client logout, Clear previous security context, so client can't access the API
                    logoutConfigurer.logoutSuccessHandler(
                            (request, response, authentication) -> SecurityContextHolder.clearContext()
                    );
                });

        // Deny Access to secured APIs Endpoints, if JWT Token expires
        httpSecurity
                .exceptionHandling(exception ->
                        exception.authenticationEntryPoint(customAuthenticationEntryPoint)
                                .accessDeniedHandler(new CustomAccessDeniedHandler())
                );

        return httpSecurity.build();
    }
}
