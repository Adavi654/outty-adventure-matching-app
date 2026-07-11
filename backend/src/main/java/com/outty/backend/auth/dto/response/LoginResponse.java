package com.outty.backend.auth.dto.response;

public record LoginResponse(
        String token,
        Long userId,
        String email,
        String role
) {
}
