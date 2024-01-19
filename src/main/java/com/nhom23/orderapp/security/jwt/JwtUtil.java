package com.nhom23.orderapp.security.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SecureDigestAlgorithm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import javax.crypto.SecretKey;

import java.util.Date;

@Component
public class JwtUtil {
    private static final Logger logger = LoggerFactory.getLogger(JwtUtil.class);
    @Value("${jwtSecret}")
    private String jwtSecret;
    public String generateAccessTokenFromAccount(String email,String url){
        return Jwts
                .builder()
                .header()
                .type("JWT")
                .and()
                .subject(email)
                .issuer(url)
                .issuedAt(new Date())
                .signWith(key())
                .compact();
    }
    private SecretKey key(){
        byte[] keyBytes = jwtSecret.getBytes();
        return Keys.hmacShaKeyFor(keyBytes);
    }
    String generateEmailFromToken(String token){
            return Jwts
                    .parser()
                    .verifyWith(key())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload()
                    .getSubject();
    }
    void validateToken(String token) {
        Jwts.parser().verifyWith(key()).build().parseSignedClaims(token);
    }
}
