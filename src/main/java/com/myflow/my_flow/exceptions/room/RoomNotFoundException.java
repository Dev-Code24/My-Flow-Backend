package com.myflow.my_flow.exceptions.room;

import com.myflow.my_flow.exceptions.constants.ErrorMessages;

public class RoomNotFoundException extends RuntimeException {
  public RoomNotFoundException() { super(ErrorMessages.ROOM_NOT_FOUND.getValue()); }
}
