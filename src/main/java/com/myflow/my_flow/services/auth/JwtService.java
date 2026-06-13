package com.myflow.my_flow.services.auth;

import com.myflow.my_flow.models.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.time.Duration;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {
  @Value("${security.jwt.secret-key}")
  private String secretKey;

  @Value("${security.jwt.expiration-time}")
  private Duration jwtExpiration;

  private Key getSignInKey() {
    byte[] keyInBytes = Decoders.BASE64.decode(this.secretKey);
    return Keys.hmacShaKeyFor(keyInBytes);
  }

  private String buildToken(Map<String, Object> extraClaims, User user, Duration duration) {
    return Jwts.builder()
        .setClaims((extraClaims))
        .setSubject(user.getEmail())
        .setIssuedAt(new Date(System.currentTimeMillis()))
        .setExpiration(new Date(System.currentTimeMillis() + jwtExpiration.toMillis()))
        .signWith(getSignInKey())
        .compact();
  }

  private Claims extractAllClaims(String token) {
    try {
      return Jwts.parserBuilder().setSigningKey(this.getSignInKey()).build().parseClaimsJws(token).getBody();
    } catch (ExpiredJwtException e) {
      return e.getClaims();
    }
  }

  private Date extractExpiration(String token) {
    return this.extractClaim(token, Claims::getExpiration);
  }

  public String generateToken(Map<String, Object> extraClaims, User user) {
    return this.buildToken(extraClaims, user, this.jwtExpiration);
  }

  public String generateToken(User user) {
    return this.generateToken(new HashMap<>(), user);
  }

  public<T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
    final Claims claims = this.extractAllClaims(token);
    return claimsResolver.apply(claims);
  }

  public String extractEmail(String token) {
    return this.extractClaim(token, Claims::getSubject);
  }


  public boolean isTokenExpired(String token) {
    return this.extractExpiration((token)).before(new Date());
  }
}
