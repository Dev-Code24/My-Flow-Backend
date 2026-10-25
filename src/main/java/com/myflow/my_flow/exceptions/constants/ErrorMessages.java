package com.myflow.my_flow.exceptions.constants;

import lombok.Getter;

@Getter
public enum ErrorMessages {
  ROOM_NOT_FOUND("Room not found"),
  ROOM_EXPIRED("Room has expired"),
  FLOW_NOT_FOUND("Flow not found"),
  DISPlAY_NAME_NOT_FOUND("Display name is required for guest users"),
  DISPlAY_NAME_LENGTH_INCORRECT("Display name must be between 2 and 50 characters"),
  INTERNAL_SERVER_ERROR("Something bad happened while handling your request. Please try again later.");

  private final String value;
  ErrorMessages(String value) { this.value = value; }
}
