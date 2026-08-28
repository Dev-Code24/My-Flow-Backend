package com.myflow.my_flow.exceptions.constants;

import lombok.Getter;

@Getter
public enum UserErrorMessages {
  EMAIL_ALREADY_EXISTS("This email already is in use.");

  private final String value;
  UserErrorMessages(String value) { this.value = value; }
}
