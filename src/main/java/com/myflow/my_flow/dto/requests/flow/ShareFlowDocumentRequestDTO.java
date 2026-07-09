package com.myflow.my_flow.dto.requests.flow;

import com.myflow.my_flow.dto.commons.flow.FlowDocumentDTO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public record ShareFlowDocumentRequestDTO(
    @NotNull
    @Valid
    FlowDocumentDTO document
) {}