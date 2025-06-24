package com.totfd.lms.service;

import com.totfd.lms.entity.Users;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String jwtSecret;

    private static final long EMAIL_VERIFICATION_EXPIRATION = 1000 * 60 * 60; // 1 hour
    private static final long LOGIN_TOKEN_EXPIRATION = 1000 * 60 * 60 * 24;   // 24 hours

    private Key getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(jwtSecret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    // Generate token for login
    public String generateToken(Users user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("email", user.getEmail());
        claims.put("role", user.getRole().getName());

        return buildToken(claims, user.getEmail(), LOGIN_TOKEN_EXPIRATION);
    }

    // Generate token for email verification
    public String generateEmailVerificationToken(String email) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("email", email);

        return buildToken(claims, email, EMAIL_VERIFICATION_EXPIRATION);
    }

    // Internal token builder
    private String buildToken(Map<String, Object> claims, String subject, long expiration) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    // Extract email/username from token
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // Validate token (subject matches and not expired)
    public boolean isTokenValid(String token, org.springframework.security.core.userdetails.UserDetails userDetails) {
        final String username = extractUsername(token);
        return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

    // Check expiration
    public boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    // Get expiration date
    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    // Generic claim extractor
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    // Parse and validate token signature
    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public String validateAndGetEmail(String token) {
        try {
            Claims claims = extractAllClaims(token);

            // Validate expiration
            if (isTokenExpired(token)) {
                throw new ExpiredJwtException(null, claims, "Token has expired");
            }

            // Subject is your email
            return claims.getSubject();
        } catch (JwtException e) {
            throw new RuntimeException("Invalid token: " + e.getMessage());
        }
    }

}
