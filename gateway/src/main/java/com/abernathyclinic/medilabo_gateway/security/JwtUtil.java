package com.abernathyclinic.medilabo_gateway.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;

import java.security.Key;
import java.util.Date;

public class JwtUtil {

    private static final String SECRET = "my-super-secret-key-my-super-secret-key";
    private static final long EXPIRATION = 1000 * 60 * 60; // 1 hour

    private static final Key KEY = Keys.hmacShaKeyFor(SECRET.getBytes());

    public static String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION))
                .signWith(KEY, SignatureAlgorithm.HS256)
                .compact();
    }

    public static Claims validateToken(String token) {
        // Use the older parser() API and pass the raw key bytes to support environments
        // where parserBuilder() is not available on the Jwts class.
        return Jwts.parser()
                .setSigningKey(KEY.getEncoded())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
