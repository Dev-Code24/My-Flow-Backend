package com.myflow.my_flow.exceptions.room;

import com.myflow.my_flow.exceptions.constants.ErrorMessages;

public class DisplayNameNotFoundException extends RuntimeException {
  public DisplayNameNotFoundException() {
    super(ErrorMessages.DISPlAY_NAME_NOT_FOUND.getValue());
  }
}
