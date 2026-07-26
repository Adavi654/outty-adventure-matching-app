package com.outty.backend.matching.dto.response;

public record SwipeResponse(
    boolean isMatch,
    Long matchedUserId
) {}