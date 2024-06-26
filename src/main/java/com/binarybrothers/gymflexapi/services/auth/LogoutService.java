package com.binarybrothers.gymflexapi.services.auth;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;

@Service
public class LogoutService implements LogoutHandler {

  private final TokenBlacklistService tokenBlacklistService;

  public LogoutService(TokenBlacklistService tokenBlacklistService) {
    this.tokenBlacklistService = tokenBlacklistService;
  }

  @Override
  public void logout(
      HttpServletRequest request,
      HttpServletResponse response,
      Authentication authentication
  ) {
    final String authHeader = request.getHeader("Authorization");
    final String jwt;
    if (authHeader == null ||!authHeader.startsWith("Bearer ")) {
      return;
    }
    jwt = authHeader.substring(7);
    SecurityContext context = SecurityContextHolder.getContext();

    tokenBlacklistService.addToBlacklist(jwt);
      SecurityContextHolder.clearContext();
    }
  }

