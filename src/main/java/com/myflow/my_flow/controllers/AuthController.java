package com.myflow.my_flow.controllers;

import com.myflow.my_flow.dto.requests.auth.AuthenticatingUserDTO;
import com.myflow.my_flow.dto.requests.auth.RegisteringUserDTO;
import com.myflow.my_flow.dto.responses.BasicResponseDTO;
import com.myflow.my_flow.dto.responses.auth.AuthenticatedUserDTO;
import com.myflow.my_flow.dto.responses.auth.RegisteredUserDTO;
import com.myflow.my_flow.models.User;
import com.myflow.my_flow.services.auth.AuthService;
import com.myflow.my_flow.services.auth.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.time.Duration;
import java.util.function.Supplier;

@RequestMapping("/auth")
@RestController
public class AuthController {
  private final JwtService jwtService;
  private final AuthService authService;

  public AuthController(
      JwtService jwtService,
      AuthService authService
  ) {
    this.jwtService = jwtService;
    this.authService = authService;
  }

  @PostMapping("/signup")
  public ResponseEntity<BasicResponseDTO<RegisteredUserDTO>> signUp(
      @NotNull @RequestBody RegisteringUserDTO user,
      HttpServletRequest req
  ) {
    // TODO: Search what is actually sent in selfLink property in production backends
    User newUser = this.authService.signUp(user);

    return this.issueTokensAndRespond(newUser, () ->
        BasicResponseDTO.<RegisteredUserDTO>builder()
            .message("success")
            .data(new RegisteredUserDTO(newUser.getEmail(), newUser.getName()))
            .build()
    );
  }
  @PostMapping("/login")
  public ResponseEntity<BasicResponseDTO<AuthenticatedUserDTO>> login(
      @RequestBody AuthenticatingUserDTO user,
      HttpServletRequest req
  ) {
    User authenticatedUser = this.authService.authenticate(user);
    return this.issueTokensAndRespond(authenticatedUser, () -> BasicResponseDTO.<AuthenticatedUserDTO>builder()
            .message("success")
            .data(new AuthenticatedUserDTO(
                authenticatedUser.getEmail(),
                authenticatedUser.getUsername()
            )
        ).build()
    );
  }

  @PostMapping("/logout")
  public ResponseEntity<BasicResponseDTO<Null>> logout (
      Principal userPrincipal
  ) {
    Authentication auth = (Authentication) userPrincipal;
    User user = (User) auth.getPrincipal();
    this.authService.logout(user);

    ResponseCookie cookie = ResponseCookie.from("access_token", "")
        .httpOnly(true)
        .secure(true)
        .path("/")
        .sameSite("None")
        .maxAge(0)
        .build();

    return ResponseEntity
        .ok()
        .header("Set-Cookie", cookie.toString())
        .body(BasicResponseDTO.<Null>builder().message("success").data(null).build());
  }

  private <T> ResponseEntity<T> issueTokensAndRespond (
      User user,
      Supplier<T> bodyFactory
  ) {
    final String jwt = this.jwtService.generateToken(user);
    ResponseCookie cookie = ResponseCookie.from("access_token", jwt)
        .httpOnly(true)
        .secure(true)
        .path("/")
        .sameSite("None")
        .maxAge(Duration.ofSeconds(this.jwtService.getJwtExpiration().toSeconds()))
        .build();

    T body = bodyFactory.get();
    return ResponseEntity
        .ok()
        .header("Set-Cookie", cookie.toString())
        .body(body);
  }
}

