package com.outty.backend.profile.dto.response;

import com.outty.backend.profile.entity.enums.InterestedIn;
import com.outty.backend.profile.entity.enums.RelationshipGoal;
import com.outty.backend.profile.entity.enums.Gender;

import java.time.LocalDate;

public record ProfileResponse(
        Long id,
        Long userId,
        String firstName,
        String lastName,
        String city,
        String state,
        String country,
        Gender gender,
        LocalDate birthDate,
        String bio,
        InterestedIn interestedIn,
        RelationshipGoal relationshipGoal
) {
}
