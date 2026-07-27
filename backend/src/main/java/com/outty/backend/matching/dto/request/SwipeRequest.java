package com.outty.backend.matching.dto.request;

public record SwipeRequest(
    Long targetId,
    String decision
) {}