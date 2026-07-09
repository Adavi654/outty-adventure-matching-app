package com.outty.backend.auth.dto.response;

public record RegisterResponse(
        Long id,
        String firstName,
        String lastName,
        String email
) {
}
