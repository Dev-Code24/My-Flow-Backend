package com.myflow.my_flow.controllers;

import com.myflow.my_flow.dto.responses.BasicResponseDTO;
import com.myflow.my_flow.dto.responses.auth.AuthenticatedUserDTO;
import com.myflow.my_flow.models.User;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/users")
@RestController
public class UserController {

  @GetMapping("/me")
  public ResponseEntity<BasicResponseDTO<AuthenticatedUserDTO>> getUser(HttpServletRequest req) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    User user = (User) authentication.getPrincipal();

    return ResponseEntity.ok(BasicResponseDTO.<AuthenticatedUserDTO>builder()
        .message("success")
        .data(new AuthenticatedUserDTO(user.getEmail(), user.getName())
        ).build()
    );
  }
}
