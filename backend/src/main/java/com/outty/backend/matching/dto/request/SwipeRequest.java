package com.outty.backend.matching.dto.request;

public record SwipeRequest(
    Long targetId,
    String decision // Expecting "INTERESTED" or "REJECT" (or lowercase "interested"/"reject")
) {}