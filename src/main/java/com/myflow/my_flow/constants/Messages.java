package com.myflow.my_flow.constants;

import lombok.Getter;

@Getter
public enum Messages {
  SUCCESS("success"),
  ERROR("error");

  private final String value;
  Messages(String value) { this.value = value; }
}
