package com.outty.backend.profile.mapper;

import com.outty.backend.profile.entity.Profile;
import com.outty.backend.profile.dto.request.ProfileRequest;
import com.outty.backend.profile.dto.response.ProfileResponse;
import com.outty.backend.profile.dto.request.UpdateProfileRequest;
import com.outty.backend.auth.entity.User;

public class ProfileMapper {
    private ProfileMapper() {
    }

    public static Profile toEntity(ProfileRequest request, User user) {
        return Profile.builder()
        .user(user)
        .city(request.city())
        .state(request.state())
        .country(request.country())
        .birthDate(request.birthDate())
        .bio(request.bio())
        .gender(request.gender())
        .interestedIn(request.interestedIn())
        .relationshipGoal(request.relationshipGoal())
        .photos(request.photos() == null ? new java.util.ArrayList<>() : new java.util.ArrayList<>(request.photos()))
        .instagramUrl(normalizeUrl(request.instagramUrl()))
        .facebookUrl(normalizeUrl(request.facebookUrl()))
        .xUrl(normalizeUrl(request.xUrl()))
        .build();
    }

    public static ProfileResponse toProfileResponse(Profile profile) {
        return new ProfileResponse(
            profile.getId(),
            profile.getUser().getId(),
            profile.getUser().getFirstName(),
            profile.getUser().getLastName(),
            profile.getCity(),
            profile.getState(),
            profile.getCountry(),
            profile.getGender(),
            profile.getBirthDate(),
            profile.getBio(),
            profile.getInterestedIn(),
            profile.getRelationshipGoal(),
            profile.getPhotos() == null ? java.util.List.of() : profile.getPhotos(),
            profile.getInstagramUrl(),
            profile.getFacebookUrl(),
            profile.getXUrl()
        );
    }

    public static void updateEntity(Profile profile, UpdateProfileRequest request) {
        profile.setCity(request.city());
        profile.setState(request.state());
        profile.setCountry(request.country());
        profile.setGender(request.gender());
        profile.setBirthDate(request.birthDate());
        profile.setBio(request.bio());
        profile.setInterestedIn(request.interestedIn());
        profile.setRelationshipGoal(request.relationshipGoal());
        profile.setPhotos(request.photos() == null ? new java.util.ArrayList<>() : new java.util.ArrayList<>(request.photos()));
        profile.setInstagramUrl(normalizeUrl(request.instagramUrl()));
        profile.setFacebookUrl(normalizeUrl(request.facebookUrl()));
        profile.setXUrl(normalizeUrl(request.xUrl()));
    }

    private static String normalizeUrl(String url) {
        if (url == null || url.isBlank()) {
            return null;
        }

        return url.trim();
    }
}
