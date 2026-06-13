package com.myflow.my_flow.services.auth;

import com.myflow.my_flow.models.User;
import com.myflow.my_flow.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserService {
  private final UserRepository userRepository;

  public UserService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  public User getUserById(UUID id) {
    return this.userRepository.findById(id).orElse(null);
  }

  public User getUserByEmail(String email) {
    return this.userRepository.findByEmail(email).orElse(null);
  }

  public boolean updateUser(User user) {
    try {
      this.userRepository.save(user);
      return true;
    } catch (Exception e) {
      return false;
    }
  }
}
