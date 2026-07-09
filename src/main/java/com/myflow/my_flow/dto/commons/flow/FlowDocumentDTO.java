package com.myflow.my_flow.dto.commons.flow;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public record FlowDocumentDTO(
    @NotNull
    Integer schemaVersion,

    @NotNull
    @Valid
    CanvasDTO canvas
) {}