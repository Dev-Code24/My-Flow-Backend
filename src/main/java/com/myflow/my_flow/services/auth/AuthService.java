package com.myflow.my_flow.services.auth;

import com.myflow.my_flow.dto.requests.auth.AuthenticatingUserDTO;
import com.myflow.my_flow.dto.requests.auth.RegisteringUserDTO;
import com.myflow.my_flow.exceptions.constants.UserErrorMessages;
import com.myflow.my_flow.exceptions.user.UserAlreadyExistsException;
import com.myflow.my_flow.models.User;
import com.myflow.my_flow.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final AuthenticationManager authenticationManager;

  public AuthService(
      UserRepository userRepository,
      PasswordEncoder passwordEncoder,
      AuthenticationManager authenticationManager
  ) {
    this.userRepository = userRepository;
    this.passwordEncoder = passwordEncoder;
    this.authenticationManager = authenticationManager;
  }

  public User signUp(RegisteringUserDTO user) {
    User registeredUser = this.userRepository.findByEmail(user.getEmail()).orElse(null);

    if (registeredUser != null) {
      throw new UserAlreadyExistsException(UserErrorMessages.EMAIL_ALREADY_EXISTS.getValue());
    }

    User newUser = new User()
        .setName(user.getUsername())
        .setEmail(user.getEmail())
        .setPassword(this.passwordEncoder.encode(user.getPassword()));

    return this.userRepository.save(newUser);
  }

  public User authenticate(AuthenticatingUserDTO user) {
    this.authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword())
    );
    User authenticatedUser = this.userRepository.findByEmail(user.getEmail()).orElseThrow();
    return this.userRepository.save(authenticatedUser);
  }

  public void logout(User user) {
    this.userRepository.save(user);
  }
}
