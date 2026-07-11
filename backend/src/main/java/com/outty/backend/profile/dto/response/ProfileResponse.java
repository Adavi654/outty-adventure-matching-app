package com.outty.backend.profile.dto.response;

import com.outty.backend.profile.entity.enums.InterestedIn;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record ProfileResponse(
        Long id,
        Long userId,
        String city,
        String state,
        String country,
        String gender,
        LocalDate birthDate,
        String bio,
        InterestedIn interestedIn,
        String relationshipGoal,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
