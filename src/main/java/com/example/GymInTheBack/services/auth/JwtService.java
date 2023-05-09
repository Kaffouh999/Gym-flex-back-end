package com.example.GymInTheBack.services.auth;

import com.example.GymInTheBack.entities.OnlineUser;
import com.example.GymInTheBack.services.mappers.OnlineUserMapper;
import com.example.GymInTheBack.services.user.OnlineUserService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.security.core.GrantedAuthority;

import java.security.Key;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class JwtService {

  @Value("${application.security.jwt.secret-key}")
  private String secretKey;
  @Value("${application.security.jwt.expiration}")
  private long jwtExpiration;
  @Value("${application.security.jwt.refresh-token.expiration}")
  private long refreshExpiration;

  private final TokenBlacklistService tokenBlacklistService;

  private final OnlineUserService onlineUserService;

  private final OnlineUserMapper onlineUserMapper;

  public JwtService(TokenBlacklistService tokenBlacklistService, OnlineUserService onlineUserService, OnlineUserMapper onlineUserMapper) {
    this.tokenBlacklistService = tokenBlacklistService;
    this.onlineUserService = onlineUserService;
    this.onlineUserMapper = onlineUserMapper;
  }

  public String getSecretKey() {
    return secretKey;
  }

  public String extractUsername(String token) {
    return extractClaim(token, Claims::getSubject);
  }

  public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
    final Claims claims = extractAllClaims(token);
    return claimsResolver.apply(claims);
  }

  public String generateToken(UserDetails userDetails,Long idUser) {
    return generateToken(new HashMap<>(), userDetails, idUser);
  }

  public String generateToken(
      Map<String, Object> extraClaims,
      UserDetails userDetails
          ,Long idUser

  ) {
    Collection<String> authorities = userDetails.getAuthorities()
            .stream()
            .map(GrantedAuthority::getAuthority)
            .collect(Collectors.toList());
    Map<String,Object> info = new HashMap<>();
    info.put("authorities", authorities);
    OnlineUser codedUser = onlineUserService.findById(idUser).get();
    codedUser.setLogin(null);
    codedUser.setPassword(null);

    info.put("user", onlineUserMapper.toDto(codedUser));
    return buildToken(info, userDetails, jwtExpiration);
  }

  public String generateRefreshToken(
      UserDetails userDetails
  ) {
    Collection<String> authorities = userDetails.getAuthorities()
            .stream()
            .map(GrantedAuthority::getAuthority)
            .collect(Collectors.toList());
    Map<String,Object> info = new HashMap<>();
    info.put("authorities", authorities);
    return buildToken(info, userDetails, refreshExpiration);
  }

  private String buildToken(
          Map<String, Object> extraClaims,
          UserDetails userDetails,
          long expiration
  ) {
    System.out.println(extraClaims.toString());
    return Jwts
            .builder()
            .setClaims(extraClaims)
            .setSubject(userDetails.getUsername())
            .setIssuedAt(new Date(System.currentTimeMillis()))
            .setExpiration(new Date(System.currentTimeMillis() + expiration))
            .signWith(getSignInKey(), SignatureAlgorithm.HS256)
            .compact();
  }

  public boolean isTokenValid(String token, UserDetails userDetails) {
    final String username = extractUsername(token);
    return (username.equals(userDetails.getUsername())) && !isTokenExpired(token) && !tokenBlacklistService.isTokenBlacklisted(token);
  }

  private boolean isTokenExpired(String token) {
    return extractExpiration(token).before(new Date());
  }

  private Date extractExpiration(String token) {
    return extractClaim(token, Claims::getExpiration);
  }

  private Claims extractAllClaims(String token) {
    return Jwts
        .parserBuilder()
        .setSigningKey(getSignInKey())
        .build()
        .parseClaimsJws(token)
        .getBody();
  }

  private Key getSignInKey() {
    byte[] keyBytes = Decoders.BASE64.decode(secretKey);
    return Keys.hmacShaKeyFor(keyBytes);
  }
}
