package com.outty.backend.profile.dto.response;

import java.time.LocalDateTime;

public record ProfileResponse(
        Long id,
        Long userId,
        String displayName,
        String location,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
