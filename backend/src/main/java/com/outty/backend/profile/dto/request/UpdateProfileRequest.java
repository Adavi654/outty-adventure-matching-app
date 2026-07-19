package com.outty.backend.profile.dto.request;

import com.outty.backend.profile.entity.enums.Gender;
import com.outty.backend.profile.entity.enums.InterestedIn;
import com.outty.backend.profile.entity.enums.RelationshipGoal;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record UpdateProfileRequest(
        @Size(max = 100, message = "City must be at most 100 characters")
        String city,

        @Size(max = 100, message = "State must be at most 100 characters")
        String state,

        @Size(max = 100, message = "Country must be at most 100 characters")
        String country,

        Gender gender,

        LocalDate birthDate,
        
        @Size(max = 500, message = "Bio must be at most 500 characters")
        String bio,

        InterestedIn interestedIn,

        RelationshipGoal relationshipGoal,

        @Size(
                max = 255,
                message = "Instagram URL must be at most 255 characters"
        )
        @Pattern(
                regexp = "^(?:\\s*|https://(?:www\\.)?instagram\\.com/[A-Za-z0-9._]+/?(?:[?#].*)?)$",
                message = "Instagram URL must be a valid HTTPS Instagram profile URL"
        )
        String instagramUrl,

        @Size(
                max = 255,
                message = "Facebook URL must be at most 255 characters"
        )
        @Pattern(
                regexp = "^(?:\\s*|https://(?:www\\.)?facebook\\.com/[A-Za-z0-9._-]+/?(?:[?#].*)?)$",
                message = "Facebook URL must be a valid HTTPS Facebook profile URL"
        )
        String facebookUrl,

        @Size(
                max = 255,
                message = "X URL must be at most 255 characters"
        )
        @Pattern(
                regexp = "^(?:\\s*|https://(?:www\\.)?(?:x\\.com|twitter\\.com)/[A-Za-z0-9_]{1,15}/?(?:[?#].*)?)$",
                message = "X URL must be a valid HTTPS X or Twitter profile URL"
        )
        String xUrl
) {    
}
