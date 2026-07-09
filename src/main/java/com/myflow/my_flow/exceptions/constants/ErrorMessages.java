package com.myflow.my_flow.exceptions.constants;

import lombok.Getter;

@Getter
public enum ErrorMessages {
  ROOM_NOT_FOUND("Room not found"),
  ROOM_EXPIRED("Room has expired"),
  FLOW_NOT_FOUND("Flow not found");

  private final String value;
  ErrorMessages(String value) { this.value = value; }
}
