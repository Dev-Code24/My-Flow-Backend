package com.myflow.my_flow.exceptions.room;

import com.myflow.my_flow.exceptions.constants.ErrorMessages;

public class RoomExpiredException extends RuntimeException {
  public RoomExpiredException() {
    super(ErrorMessages.ROOM_EXPIRED.getValue());
  }
}
