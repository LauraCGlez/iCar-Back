package com.back.config;

import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

public class JwtSecurityContext {
    public static final String JWT_KEY = String.valueOf(Keys.secretKeyFor(SignatureAlgorithm.HS256));;
    public static final String JWT_HEADER = "Authorization";
}
