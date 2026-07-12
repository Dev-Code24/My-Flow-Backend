package com.myflow.my_flow.exceptions;

import com.myflow.my_flow.dto.responses.BasicResponseDTO;
import com.myflow.my_flow.exceptions.flow.FlowNotFoundException;
import com.myflow.my_flow.exceptions.room.RoomExpiredException;
import com.myflow.my_flow.exceptions.room.RoomNotFoundException;
import com.myflow.my_flow.types.RoomDuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConversionException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(HttpMessageConversionException.class)
  public ResponseEntity<BasicResponseDTO<Void>> handleInvalidRequest(
      HttpMessageConversionException exception
  ) {
    String message = "Invalid request body";

    if (isInvalidRoomDuration(exception)) {
      message = "Duration must be one of: HALF_HOUR, ONE_HOUR, THREE_HOURS";
    }

    return error(
        HttpStatus.BAD_REQUEST,
        message
    );
  }

  @ExceptionHandler(MethodArgumentTypeMismatchException.class)
  public ResponseEntity<BasicResponseDTO<Void>> handleTypeMismatch(
      MethodArgumentTypeMismatchException exception
  ) {
    return error(
        HttpStatus.BAD_REQUEST,
        "Invalid value for parameter: " + exception.getName()
    );
  }

  @ExceptionHandler({
      RoomNotFoundException.class,
      FlowNotFoundException.class
  })
  public ResponseEntity<BasicResponseDTO<Void>> handleResourceNotFound(
      RuntimeException exception
  ) {
    return error(
        HttpStatus.NOT_FOUND,
        exception.getMessage()
    );
  }

  @ExceptionHandler(RoomExpiredException.class)
  public ResponseEntity<BasicResponseDTO<Void>> handleRoomExpired(
      RoomExpiredException exception
  ) {
    return error(
        HttpStatus.BAD_REQUEST,
        exception.getMessage()
    );
  }

  private boolean isInvalidRoomDuration(HttpMessageConversionException exception) {
    String message = exception.getMessage();
    return message != null && message.contains(RoomDuration.class.getName());
  }

  private ResponseEntity<BasicResponseDTO<Void>> error(
      HttpStatus status,
      String message
  ) {
    return ResponseEntity.status(status).body(
        BasicResponseDTO.<Void>builder()
            .message(message)
            .build()
    );
  }
}