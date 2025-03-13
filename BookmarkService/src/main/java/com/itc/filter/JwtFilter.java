package com.itc.filter;

import java.io.IOException;
import java.util.Date;

import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class JwtFilter extends OncePerRequestFilter {

    private final String secretKey = "YourVerySecretKeyThatShouldBeAtLeast32BytesLong"; // Replace with your actual secret key

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String authorizationHeader = request.getHeader("Authorization");

        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Missing or invalid Authorization header");
            return;
        }

        String token = authorizationHeader.substring(7);

        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(secretKey)
                    .parseClaimsJws(token)
                    .getBody();

            if (claims.getExpiration().before(new Date())) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token has expired");
                return;
            }

        } catch (SignatureException e) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid token");
            return;
        }

        filterChain.doFilter(request, response);
    }

 
}
