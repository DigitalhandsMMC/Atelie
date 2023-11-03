package com.example.clothesshop.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.MalformedJwtException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class JwtService {

    public static final String SECRET_KEY = "1234512345123451234512345123451234512345123451234512345123451234";

    @Value("${jwt.expiration}")
    private long EXPIRATION_TIME;

    public String generateToken(String email) {
        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS512, SECRET_KEY)
                .compact();
    }

    public Claims decodeToken(String token) {
        return Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody();
    }

    public String extractEmail(String token) {
        return decodeToken(token).getSubject();
    }


    public boolean isTokenExpired(String token) {
        return decodeToken(token).getExpiration().before(new Date());
    }

    public boolean validateToken(String token) {
        try {
            Claims claims = decodeToken(token);
            return !isTokenExpired(token);
        } catch (SignatureException e) {
            // Invalid JWT signature
            return false;
        } catch (MalformedJwtException e) {
            // Invalid JWT token
            return false;
        } catch (ExpiredJwtException e) {
            // Expired JWT token
            return false;
        } catch (Exception e) {
            // Other exceptions
            return false;
        }
    }
}
