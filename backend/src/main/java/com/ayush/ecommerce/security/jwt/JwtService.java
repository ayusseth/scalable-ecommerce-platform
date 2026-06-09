package com.ayush.ecommerce.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService
{
    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.expiration}")
    private long jwtExpiration;

    private SecretKey secretKey;

    @PostConstruct
    public void init(){
        this.secretKey=
                Keys.hmacShaKeyFor(jwtSecret.getBytes());
    }


    public String extractUsername(String token){
        return extractClaim(token, Claims::getSubject);
    }

    public String generateToken(String email){
        return  generateToken(new HashMap<>(), email);
    }

    public String generateToken(Map<String, Object> extraClaims,
                                String email){
        return Jwts.builder()
                .claims(extraClaims)
                .subject(email)
                .issuedAt(new Date())
                .expiration(
                        new Date(
                                System.currentTimeMillis()
                                + jwtExpiration
                        )
                )
                .signWith(secretKey)
                .compact();
    }

    public <T> T extractClaim(
            String token,
            Function<Claims, T> claimsResolver
    ){
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token){
        return Jwts
                .parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public boolean isTokenExpired(String token){
        return extractExpiration(token)
                    .before(new Date());

    }

    public Date extractExpiration(String token){
        return extractClaim(
                token, Claims::getExpiration
        );
    }
}
