package com.myflow.my_flow.dto.commons.flow;

import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.Map;

public record CanvasDTO(
    @NotNull
    List<Map<String, Object>> elements
) {}
