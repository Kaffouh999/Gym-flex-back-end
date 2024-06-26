package com.binarybrothers.gymflexapi.controllers;

import com.binarybrothers.gymflexapi.services.auth.AuthenticationService;
import com.binarybrothers.gymflexapi.utils.auth.AuthenticationRequest;
import com.binarybrothers.gymflexapi.utils.auth.AuthenticationResponse;
import com.binarybrothers.gymflexapi.utils.auth.RegisterRequest;

import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import lombok.RequiredArgsConstructor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Map;

/**
 * Controller class for handling authentication related endpoints.
 */
@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class AuthenticationController {

    Logger log = LoggerFactory.getLogger(AuthenticationController.class);

    private final AuthenticationService service;

    /**
     * Endpoint for user registration.
     *
     * @param request The registration request body.
     * @return ResponseEntity containing the authentication response.
     * @throws MessagingException If an error occurs during the registration process.
     */
    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody RegisterRequest request) throws MessagingException {
        log.debug("REST request to register : {}", request);
        AuthenticationResponse response = service.register(request, null);
        return ResponseEntity.ok(response);
    }

    /**
     * Endpoint for user authentication.
     *
     * @param request The authentication request body.
     * @return ResponseEntity containing the authentication response.
     */
    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request) {
        log.debug("REST request to authenticate : {}", request);
        AuthenticationResponse response = service.authenticate(request);
        return ResponseEntity.ok(response);
    }

    /**
     * Endpoint for refreshing authentication token.
     *
     * @param request       The HTTP request.
     * @param response      The HTTP response.
     * @param tokenRequest  The token refresh request body.
     * @throws IOException  If an I/O error occurs.
     */
    @PostMapping("/refresh-token")
    public void refreshToken(HttpServletRequest request, HttpServletResponse response, @RequestBody Map<String, String> tokenRequest) throws IOException {
        log.debug("REST request to refresh token : {}", tokenRequest);
        String refreshToken = tokenRequest.get("refresh_token");
        service.refreshToken(request, response, refreshToken);
    }
}
