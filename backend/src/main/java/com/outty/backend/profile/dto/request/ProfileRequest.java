package com.outty.backend.profile.dto.request;

import com.outty.backend.profile.entity.enums.InterestedIn;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record ProfileRequest(
        @NotBlank(message = "City is required")
        @Size(max = 100, message = "City must be at most 100 characters")
        String city,

        @NotBlank(message = "State is required")
        @Size(max = 100, message = "State must be at most 100 characters")
        String state,

        @NotBlank(message = "Country is required")
        @Size(max = 100, message = "Country must be at most 100 characters")
        String country,

        @NotBlank(message = "Gender is required")
        @Size(max = 50, message = "Gender must be at most 50 characters")
        String gender,

        LocalDate birthDate,

        @NotBlank(message = "Bio is required")
        @Size(max = 500, message = "Bio must be at most 500 characters")
        String bio,

        @NotNull(message = "Interested in is required")
        InterestedIn interestedIn,

        @NotBlank(message = "Relationship goal is required")
        @Size(max = 100, message = "Relationship goal must be at most 100 characters")
        String relationshipGoal
) {
}
