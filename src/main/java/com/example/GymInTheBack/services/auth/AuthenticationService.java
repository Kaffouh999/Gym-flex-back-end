package com.example.GymInTheBack.services.auth;


import com.example.GymInTheBack.entities.GymBranch;
import com.example.GymInTheBack.entities.OnlineUser;
import com.example.GymInTheBack.entities.Role;
import com.example.GymInTheBack.repositories.GymBranchRepository;
import com.example.GymInTheBack.repositories.OnlineUserRepository;
import com.example.GymInTheBack.services.mailing.EmailService;
import com.example.GymInTheBack.utils.auth.AuthenticationRequest;
import com.example.GymInTheBack.utils.auth.RegisterRequest;
import com.example.GymInTheBack.utils.auth.AuthenticationResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
  private final OnlineUserRepository repository;
  private final PasswordEncoder passwordEncoder;
  private final JwtService jwtService;
  private final AuthenticationManager authenticationManager;
  private final EmailService emailService;
  private final GymBranchRepository gymBranchRepository;

  public AuthenticationResponse register(RegisterRequest request,Role roleUser) throws MessagingException {

    var user = OnlineUser.builder()
        .firstName(request.getFirstName())
        .lastName(request.getLastName())
        .email(request.getEmail())
            .login(request.getLogin())
        .password(passwordEncoder.encode(request.getPassword()))
        .role(roleUser)
        .build();

    var savedUser = repository.save(user);

    String validationKey = savedUser.getId()+"_"+jwtService.buildToken(new HashMap<>(),user,500000000);
    savedUser.setValidationKey(validationKey);
    repository.save(savedUser);
    List<GymBranch> gymBranchList = gymBranchRepository.findAll();
    if(!gymBranchList.isEmpty()){
      GymBranch gymBranch = gymBranchList.get(0);
      String message = "Please click the link below to verify your email:\n" +
              "http://localhost:8080/api/web/verify/" + validationKey;
    //  emailService.sendEmail(gymBranch.getEmail(),gymBranch.getAppPasswordEmail(),user.getEmail(),"Email Verification",message);
    }


    var jwtToken = jwtService.generateToken(savedUser, savedUser.getId());
    var refreshToken = jwtService.generateRefreshToken(savedUser);
    return AuthenticationResponse.builder()
        .accessToken(jwtToken)
            .refreshToken(refreshToken)
        .build();
  }

  public AuthenticationResponse authenticate(AuthenticationRequest request) {
    authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(
            request.getEmail(),
            request.getPassword()
        )
    );
    var user = repository.findByEmailIsIgnoreCase(request.getEmail())
        .orElseThrow();
    var jwtToken = jwtService.generateToken(user,user.getId());
    var refreshToken = jwtService.generateRefreshToken(user);
    return AuthenticationResponse.builder()
        .accessToken(jwtToken)
            .refreshToken(refreshToken)
        .build();
  }





  public void refreshToken(
          HttpServletRequest request,
          HttpServletResponse response,
          String refreshToken
  ) throws IOException {
    final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
   // final String refreshToken;
    final String userEmail;
    if (authHeader == null ||!authHeader.startsWith("Bearer ")) {
      return;
    }
    //refreshToken = authHeader.substring(7);
    userEmail = jwtService.extractUsername(refreshToken);
    if (userEmail != null) {
      var user = this.repository.findByEmailIsIgnoreCase(userEmail)
              .orElseThrow();
      if (jwtService.isTokenValid(refreshToken, user)) {
        var accessToken = jwtService.generateToken(user,user.getId());

        var authResponse = AuthenticationResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
        new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
      }
    }
  }
}
