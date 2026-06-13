package com.myflow.my_flow.exceptions.room.constants;

import lombok.Getter;

@Getter
public enum ErrorMessages {
  ROOM_NOT_FOUND("Room not found"),
  ROOM_EXPIRED("Room has expired");

  private final String value;
  ErrorMessages(String value) { this.value = value; }
}
