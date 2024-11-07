package com.spring.vendorAPI.filters;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.spring.vendorAPI.services.interfaces.UserService;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Value("${JWT_SECRET_KEY}")
    private String secretKey;

    @Autowired
    private UserService userService;

    private SecretKey getSecretKey() {
        return Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");
        String jwtToken = null;
        String email = null;

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            jwtToken = authHeader.substring(7);
            try {
                email = Jwts.parser()
                            .setSigningKey(getSecretKey())
                            .parseClaimsJws(jwtToken)
                            .getBody()
                            .getSubject();
                System.out.println("Token valid. Email: " + email);
            } catch (Exception e) {
                System.out.println("Failed to parse JWT: " + e.getMessage());
            }
        } else {
            System.out.println("Authorization header is missing or invalid.");
        }

        if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = userService.loadUserByUsername(email);
            if (userDetails != null) {
                if (jwtToken != null) {
                    try {
                        if (Jwts.parser().setSigningKey(getSecretKey()).parseClaimsJws(jwtToken).getBody().getSubject().equals(email)) {
                            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                                    userDetails, null, userDetails.getAuthorities());
                            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                            SecurityContextHolder.getContext().setAuthentication(authentication);
                            System.out.println("User authenticated: " + email);
                        } else {
                            System.out.println("JWT token does not match the user: " + email);
                        }
                    } catch (Exception e) {
                        System.out.println("Failed to validate JWT: " + e.getMessage());
                    }
                } else {
                    System.out.println("JWT token is null.");
                }
            } else {
                System.out.println("User not found: " + email);
            }
        }
        filterChain.doFilter(request, response);
    }
}
