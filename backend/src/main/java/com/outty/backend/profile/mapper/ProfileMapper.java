package com.outty.backend.profile.mapper;

import com.outty.backend.profile.dto.response.ProfileResponse;
import com.outty.backend.profile.entity.Profile;

public class ProfileMapper {
    private ProfileMapper() {
    }

    public static ProfileResponse toProfileResponse(Profile profile) {
        return new ProfileResponse(
                profile.getId(),
                profile.getUser().getId(),
                profile.getCity(),
                profile.getState(),
                profile.getCountry(),
                profile.getGender(),
                profile.getBirthDate(),
                profile.getBio(),
                profile.getInterestedIn(),
                profile.getRelationshipGoal(),
                profile.getCreatedAt(),
                profile.getUpdatedAt()
        );
    }
}
