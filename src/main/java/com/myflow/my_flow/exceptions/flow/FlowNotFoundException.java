package com.myflow.my_flow.exceptions.flow;

import com.myflow.my_flow.exceptions.constants.ErrorMessages;

public class FlowNotFoundException extends RuntimeException {
  public FlowNotFoundException() {
    super(ErrorMessages.FLOW_NOT_FOUND.getValue());
  }
}
