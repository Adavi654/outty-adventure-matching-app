package com.outty.backend.matching.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public record SwipeResponse(
    @JsonProperty("isMatch") boolean isMatch,
    Long matchedUserId
) {}