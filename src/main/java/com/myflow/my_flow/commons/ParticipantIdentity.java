package com.myflow.my_flow.commons;

import java.util.UUID;

public record ParticipantIdentity(UUID id, Boolean isAuthenticated, String displayName) { }
