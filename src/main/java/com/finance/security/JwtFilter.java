package com.finance.security;

import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class JwtFilter extends GenericFilter {

    private final JwtUtil jwtUtil;
    private final CustomUserDetailsService userDetailsService;

    public JwtFilter(JwtUtil jwtUtil, CustomUserDetailsService userDetailsService) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;

        String authHeader = req.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);

            try {
                if (token.split("\\.").length != 3) {
                    throw new MalformedJwtException("Invalid JWT format");
                }

                String username = jwtUtil.extractUsername(token);
                var userDetails = userDetailsService.loadUserByUsername(username);

                var auth = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities()
                );

                SecurityContextHolder.getContext().setAuthentication(auth);

            } catch (MalformedJwtException e) {
                System.err.println("JWT parsing failed: " + e.getMessage());
                SecurityContextHolder.clearContext();
            } catch (Exception e) {
                System.err.println("Unexpected JWT error: " + e.getMessage());
                SecurityContextHolder.clearContext();
            }
        }

        chain.doFilter(request, response);
    }
}