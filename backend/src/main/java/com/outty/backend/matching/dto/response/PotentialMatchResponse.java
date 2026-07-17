package com.outty.backend.matching.dto.response;

import com.outty.backend.profile.entity.enums.InterestedIn;

import java.time.LocalDate;

public record PotentialMatchResponse(
        Long userId,
        String firstName,
        String photoUrl,
        String city,
        String state,
        String country,
        String gender,
        LocalDate birthDate,
        String bio,
        InterestedIn interestedIn,
        String relationshipGoal,
        boolean demoData
) {
}
