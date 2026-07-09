package com.myflow.my_flow.dto.responses.flow;

import lombok.Builder;

import java.time.Instant;
import java.util.UUID;

@Builder
public record ShareFlowResponseDTO(
    UUID flowId,
    Instant expiresAt
) { }
