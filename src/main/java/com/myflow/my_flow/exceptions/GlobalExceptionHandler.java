package com.myflow.my_flow.exceptions;

import com.myflow.my_flow.dto.responses.BasicResponseDTO;
import com.myflow.my_flow.exceptions.flow.FlowNotFoundException;
import com.myflow.my_flow.exceptions.room.RoomExpiredException;
import com.myflow.my_flow.exceptions.room.RoomNotFoundException;
import com.myflow.my_flow.types.RoomDuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConversionException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@RestControllerAdvice
public class GlobalExceptionHandler {
  @ExceptionHandler({
      HttpMessageNotReadableException.class,
      HttpMessageConversionException.class
  })
  public ResponseEntity<BasicResponseDTO<Void>> handleInvalidRequest(
      RuntimeException exception
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

  @ExceptionHandler(MethodArgumentTypeMismatchException.class)
  public ResponseEntity<BasicResponseDTO<Void>> handleTypeMismatch(
      MethodArgumentTypeMismatchException exception
  ) {
    String paramName = exception.getName();

    return ResponseEntity.badRequest().body(
        BasicResponseDTO.<Void>builder()
            .message("Invalid value for parameter: " + paramName)
            .build()
    );
  }

  @ExceptionHandler({
      RoomNotFoundException.class,
      FlowNotFoundException.class
  })
  public ResponseEntity<BasicResponseDTO<Void>> handleRoomNotFound(
      RuntimeException exception
  ) {
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
        BasicResponseDTO.<Void>builder()
            .message(exception.getMessage())
            .build()
    );
  }

  @ExceptionHandler(RoomExpiredException.class)
  public ResponseEntity<BasicResponseDTO<Void>> handleRoomExpired(
      RoomExpiredException exception
  ) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
        BasicResponseDTO.<Void>builder()
            .message(exception.getMessage())
            .build()
    );
  }
}
