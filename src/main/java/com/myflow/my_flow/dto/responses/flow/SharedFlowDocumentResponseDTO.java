package com.myflow.my_flow.dto.responses.flow;

import com.myflow.my_flow.dto.commons.flow.FlowDocumentDTO;
import lombok.Builder;

import java.time.Instant;
import java.util.UUID;

@Builder
public record SharedFlowDocumentResponseDTO(
    UUID flowId,
    FlowDocumentDTO document,
    Instant expiresAt
) {}
