package com.hask.hasktask.config;

import com.hask.hasktask.config.service.JWTService;
import com.hask.hasktask.service.AccessTokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JWTService jwtService;
    private final AccessTokenService accessTokenService;
    private final UserDetailsService userDetailsService;

    @Autowired
    public JwtAuthenticationFilter(JWTService jwtService,
                                   AccessTokenService accessTokenService,
                                   UserDetailsService userDetailsService) {
        this.jwtService = jwtService;
        this.accessTokenService = accessTokenService;
        this.userDetailsService = userDetailsService;
    }

    /**
     * This Intercept every Request Payload made by Client/user
     */
    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        // Get Authorization Token from Client Request payload Header
        final String authHeader = request.getHeader("Authorization");
        final String jwtToken;
        final String username;

        /*
         * Validate Client HttpRequest: Authorization Header doesn't have Bearer Token
         * Stop here, and Return a Status Code: UNAUTHORIZED
         * */
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            // System.out.println(request.getServletPath().contains("/api/v1/auth"));
            filterChain.doFilter(request, response);
            return;
        }

        /*
         * Extract the Token from Client/User
         * Request payload (Authorization header -> "Bearer ") */
        jwtToken = authHeader.substring(7);

        username = jwtService.extractUsername(jwtToken);

        // Allow Authorized Client/User only
        isUserAuthenticated(request, jwtToken, username);

        // Pass it for filter execution
        filterChain.doFilter(request, response);
    }

    /**
     * Intercept user/client request
     */
    private void isUserAuthenticated(HttpServletRequest request, String jwtToken, String username) {
        if (username != null
                && SecurityContextHolder.getContext().getAuthentication() == null) {

            // Check if Client/User exist in DB table
            UserDetails userDetails =
                    this.userDetailsService.loadUserByUsername(username);

            /*
             * Check if Client:
             * 1- Email Address is Confirmed
             * 2- Authorization-Header JWT/Access Token is valid,
             * Then allow access to API endpoint */
            if (userDetails.isEnabled() &&
                    jwtService.isTokenValid(jwtToken, userDetails) &&
                    accessTokenService.isUnexpiredServerToken(jwtToken)) {
                // If valid, Create AuthToken/JwtToken(Access Token) from user info
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );

                /*
                 * Associate the new Token to user/client Details
                 * Build details out of client HTTP Request
                 * */
                authenticationToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );

                // Update the Security Context Holder
                SecurityContextHolder
                        .getContext()
                        .setAuthentication(authenticationToken);
            }
        }
    }
}
