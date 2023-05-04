package com.example.GymInTheBack.services.auth;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class TokenBlacklistScheduler {

    private final TokenBlacklistService tokenBlacklistService;

    public TokenBlacklistScheduler(TokenBlacklistService tokenBlacklistService) {
        this.tokenBlacklistService = tokenBlacklistService;
    }

    @Scheduled(fixedRate = 3600000) // run every hour
    public void removeExpiredTokens() {
        tokenBlacklistService.removeExpiredTokens();
    }
}
