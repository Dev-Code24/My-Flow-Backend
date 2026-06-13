package com.myflow.my_flow.dto.responses;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

import java.time.Instant;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@Builder
public class BasicResponseDTO<Type> {
  @Builder.Default
  private Instant timestamp = Instant.now();
  private String message;
  private Type data;
}
