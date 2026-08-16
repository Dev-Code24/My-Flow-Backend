package com.myflow.my_flow.exceptions.room;

import com.myflow.my_flow.exceptions.constants.ErrorMessages;

public class DisplayNameIncorrectLengthException extends RuntimeException {
  public DisplayNameIncorrectLengthException() {
    super(ErrorMessages.DISPlAY_NAME_LENGTH_INCORRECT.getValue());
  }
}
