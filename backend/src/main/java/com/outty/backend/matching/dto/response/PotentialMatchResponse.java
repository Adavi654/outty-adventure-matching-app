package com.outty.backend.matching.dto.response;

import com.outty.backend.profile.dto.response.AdventurePreferenceResponse;
import com.outty.backend.profile.entity.enums.InterestedIn;
import com.outty.backend.profile.entity.enums.RelationshipGoal;

import java.time.LocalDate;
import java.util.List;

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
        RelationshipGoal relationshipGoal,
        List<AdventurePreferenceResponse> adventures,
        boolean demoData
) {
}
