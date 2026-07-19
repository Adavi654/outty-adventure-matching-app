package com.outty.backend.profile.dto.request;

import com.outty.backend.profile.entity.enums.AdventureType;
import com.outty.backend.profile.entity.enums.SkillLevel;
import jakarta.validation.constraints.NotNull;

public record AdventurePreferenceRequest(
        @NotNull(message = "Adventure type is required")
        AdventureType adventureType,

        @NotNull(message = "Skill level is required")
        SkillLevel skillLevel
) {
}
