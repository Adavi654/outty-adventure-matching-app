package com.outty.backend.profile.mapper;

import com.outty.backend.profile.dto.request.AdventurePreferenceRequest;
import com.outty.backend.profile.dto.response.AdventurePreferenceResponse;
import com.outty.backend.profile.entity.Profile;
import com.outty.backend.profile.dto.request.ProfileRequest;
import com.outty.backend.profile.dto.response.ProfileResponse;
import com.outty.backend.profile.dto.request.UpdateProfileRequest;
import com.outty.backend.auth.entity.User;
import com.outty.backend.profile.entity.ProfileAdventure;

import java.util.ArrayList;
import java.util.List;

public class ProfileMapper {
    private ProfileMapper() {
    }

    public static Profile toEntity(ProfileRequest request, User user) {
        Profile profile = Profile.builder()
                .user(user)
                .city(request.city())
                .state(request.state())
                .country(request.country())
                .birthDate(request.birthDate())
                .bio(request.bio())
                .gender(request.gender())
                .interestedIn(request.interestedIn())
                .relationshipGoal(request.relationshipGoal())
                .photos(request.photos() == null ? new ArrayList<>() : new ArrayList<>(request.photos()))
                .instagramUrl(normalizeUrl(request.instagramUrl()))
                .facebookUrl(normalizeUrl(request.facebookUrl()))
                .xUrl(normalizeUrl(request.xUrl()))
                .build();

        setAdventures(profile, request.adventures());

        return profile;
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
            profile.getXUrl(),
                toAdventureResponses(profile.getAdventures())
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
        setAdventures(profile, request.adventures());
    }

    private static void setAdventures(Profile profile, List<AdventurePreferenceRequest> adventures) {
        profile.getAdventures().clear();

        if (adventures == null || adventures.isEmpty()) {
            return;
        }

        for (AdventurePreferenceRequest adventure : adventures) {
            ProfileAdventure profileAdventure = ProfileAdventure.builder()
                    .profile(profile)
                    .adventureType(adventure.adventureType())
                    .skillLevel(adventure.skillLevel())
                    .build();
            profile.getAdventures().add(profileAdventure);
        }
    }

    private static List<AdventurePreferenceResponse> toAdventureResponses(List<ProfileAdventure> adventures) {
        if (adventures == null || adventures.isEmpty()) {
            return List.of();
        }

        return adventures.stream()
                .map(adventure -> new AdventurePreferenceResponse(
                        adventure.getAdventureType(),
                        adventure.getSkillLevel()
                ))
                .toList();
    }



    private static String normalizeUrl(String url) {
        if (url == null || url.isBlank()) {
            return null;
        }

        return url.trim();
    }
}
