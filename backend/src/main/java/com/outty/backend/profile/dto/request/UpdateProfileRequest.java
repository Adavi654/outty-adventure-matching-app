package com.outty.backend.profile.dto.request;

import com.outty.backend.profile.entity.enums.Gender;
import com.outty.backend.profile.entity.enums.InterestedIn;
import com.outty.backend.profile.entity.enums.RelationshipGoal;

import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.util.List;

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

        List<String> photos
) {    
}
