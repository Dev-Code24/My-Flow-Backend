package com.myflow.my_flow.commons;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record ParticipantIdentity(@NotNull UUID id, @NotNull  Boolean isAuthenticated, String displayName) { }
