package com.back.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;

@Service
public class JwtUtil {

    SecretKey key = Keys.hmacShaKeyFor(JwtSecurityContext.JWT_KEY.getBytes());

    public String generateJwtToken(Authentication authentication) {
        return Jwts.builder()
                .setIssuer("Laura")
                .setIssuedAt(new Date())
                .setExpiration(new Date(new Date().getTime() + 86400000))
                .claim("email", authentication.getName())
                .signWith(key).compact();
    }

    public String getEmailFromJwtToken(String jwt) {
        jwt = jwt.substring(7);
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(jwt)
                .getBody();
        return String.valueOf(claims.get("email"));
    }


}