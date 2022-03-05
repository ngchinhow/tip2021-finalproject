package com.tfip2021.module4.security.jwt;

import static com.tfip2021.module4.models.Constants.JWT_SECRET;
import static com.tfip2021.module4.models.Constants.JWT_EXPIRATIONMILLISEC;

import java.util.Date;
import java.util.Optional;

import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class JWTService {

    public String createJWT(Long databaseUserId) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + JWT_EXPIRATIONMILLISEC);

        return Jwts.builder().setSubject(Long.toString(databaseUserId))
            .setIssuedAt(new Date())
            .setExpiration(expiryDate)
            .signWith(SignatureAlgorithm.HS512, JWT_SECRET)
            .compact();
    }

    public Optional<Long> getUserIdFromJWT(String token) {
        try {
            Claims claims = Jwts.parser().setSigningKey(JWT_SECRET)
                .parseClaimsJws(token)
                .getBody();
            return Optional.of(Long.parseLong(claims.getSubject()));
        } catch (SignatureException ex) {
            log.error("Invalid JWT signature");
        } catch (MalformedJwtException ex) {
            log.error("Invalid JWT token");
        } catch (ExpiredJwtException ex) {
            log.error("Expired JWT token");
        } catch (UnsupportedJwtException ex) {
            log.error("Unsupported JWT token");
        } catch (IllegalArgumentException ex) {
            log.error("JWT claims string is empty.");
        }
        return Optional.empty();
    }
}
