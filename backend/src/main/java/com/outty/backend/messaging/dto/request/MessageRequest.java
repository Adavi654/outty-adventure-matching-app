package com.outty.backend.messaging.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record MessageRequest(
        @NotNull Long senderId,
        @NotNull Long receiverId,
        @NotBlank(message = "Message content is required") String content
) {
}
