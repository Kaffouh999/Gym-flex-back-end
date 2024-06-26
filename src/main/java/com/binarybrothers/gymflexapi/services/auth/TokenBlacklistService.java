package com.binarybrothers.gymflexapi.services.auth;

public interface TokenBlacklistService {
    boolean isTokenBlacklisted(String token);
    void addToBlacklist(String token);
    void removeExpiredTokens();
}
