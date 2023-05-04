package com.example.GymInTheBack.web;

import com.example.GymInTheBack.services.auth.AuthenticationService;
import com.example.GymInTheBack.utils.auth.AuthenticationRequest;
import com.example.GymInTheBack.utils.auth.AuthenticationResponse;
import com.example.GymInTheBack.utils.auth.RegisterRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class AuthenticationController {

  private final AuthenticationService service;

  @PostMapping("/register")
  public ResponseEntity<AuthenticationResponse> register(
      @RequestBody RegisterRequest request
  ) {
    return ResponseEntity.ok(service.register(request));
  }
  @PostMapping("/authenticate")
  public ResponseEntity<AuthenticationResponse> authenticate(
      @RequestBody AuthenticationRequest request
  ) {
    return ResponseEntity.ok(service.authenticate(request));
  }

  @PostMapping("/refresh-token")
  public void refreshToken(
      HttpServletRequest request,
      HttpServletResponse response,
      @RequestBody Map<String, String> refreshToken
  ) throws IOException {
    service.refreshToken(request, response,
            refreshToken.get("refresh_token"));
  }


}
