package com.icarapp.icar.security.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;

import java.security.Key;
import java.util.Base64;
import java.util.Date;

public class JwtSecurityContext {

    private static final Key JWT_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    private static final String JWT_HEADER = "Authorization";

    public static void main(String[] args) {
        // Genera una nueva clave secreta
        Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);

        System.out.println(key.getAlgorithm());

        // Convierte la clave secreta a una cadena base64
        String base64Secret = Base64.getEncoder().encodeToString(key.getEncoded());

        System.out.println(base64Secret);

        String token = Jwts.builder()
                .setSubject("user")
                .setExpiration(new Date(System.currentTimeMillis() + 3600000)) // 1 hour
                .signWith(JWT_KEY)
                .compact();

        System.out.println("Token: " + token);

        isTokenExpired(token);
        isValidToken(token);
    }

    public static void isTokenExpired(String token) {
        try {
            Jws<Claims> claimsJws = Jwts.parserBuilder().setSigningKey(JWT_KEY).build().parseClaimsJws(token);
            if (claimsJws.getBody().getExpiration().before(new Date())) {
                System.out.println("Token has expired");
            } else {
                System.out.println("Token has not expired");
            }
        } catch (ExpiredJwtException e) {
            System.out.println("Token has expired");
        } catch (JwtException e) {
            System.out.println("Failed to check if token is expired");
        }
    }

    public static void isValidToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(JWT_KEY).build().parseClaimsJws(token);
            System.out.println("Valid token");
        } catch (SignatureException e) {
            System.out.println("Invalid token signature");
        } catch (JwtException e) {
            System.out.println("Invalid token");
        }
    }
}