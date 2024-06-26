package com.binarybrothers.gymflexapi.services.auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Service
public class InMemoryTokenBlacklistService implements TokenBlacklistService {

    private final Set<String> blacklist = new HashSet<>();
    @Value("${application.security.jwt.secret-key}")
    private String secretKey;

    @Override
    public boolean isTokenBlacklisted(String token) {
        return blacklist.contains(token);
    }

    @Override
    public void addToBlacklist(String token) {
        blacklist.add(token);
    }

    @Override
    public void removeExpiredTokens() {
        long now = System.currentTimeMillis();
        blacklist.removeIf(token -> {
            JwtParser parser = Jwts.parserBuilder().setSigningKey(secretKey).build();
            try {
                Claims claims = parser.parseClaimsJws(token).getBody();
                Date expiration = claims.getExpiration();
                if (expiration != null) {
                    return expiration.getTime() < now;
                }
            } catch (JwtException e) {
            return true;
            }
            return false;
        });
    }
}

