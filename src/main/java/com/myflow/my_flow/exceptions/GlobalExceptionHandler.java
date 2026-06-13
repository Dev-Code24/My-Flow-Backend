package com.myflow.my_flow.exceptions;

import com.myflow.my_flow.dto.responses.BasicResponseDTO;
import com.myflow.my_flow.exceptions.room.RoomNotFoundException;
import com.myflow.my_flow.types.RoomDuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
  @ExceptionHandler(HttpMessageNotReadableException.class)
  public ResponseEntity<BasicResponseDTO<Void>> handleInvalidRequest(
      HttpMessageNotReadableException exception
  ) {

    String message = exception.getMessage();

    if (
        message != null &&
        message.contains(RoomDuration.class.getName())
    ) {
      return ResponseEntity.unprocessableContent().body(
          BasicResponseDTO.<Void>builder()
              .message("Duration must be one of: HALF_HOUR, ONE_HOUR, THREE_HOURS")
              .build()
      );
    }

    return ResponseEntity.badRequest().body(
        BasicResponseDTO.<Void>builder()
            .message("Invalid request body")
            .build()
    );
  }

  @ExceptionHandler(RoomNotFoundException.class)
  public ResponseEntity<BasicResponseDTO<Void>> handleRoomNotFound(
      RoomNotFoundException exception
  ) {
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
        BasicResponseDTO.<Void>builder()
            .message(exception.getMessage())
            .build()
    );
  }

}
