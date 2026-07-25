package com.outty.backend.messaging.dto.response;

import java.time.LocalDateTime;

public record MessageResponse(
        Long id,
        Long senderId,
        Long receiverId,
        String content,
        LocalDateTime createdAt
) {
}
