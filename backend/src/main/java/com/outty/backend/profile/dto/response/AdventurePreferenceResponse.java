package com.outty.backend.profile.dto.response;

import com.outty.backend.profile.entity.enums.AdventureType;
import com.outty.backend.profile.entity.enums.SkillLevel;

public record AdventurePreferenceResponse(
        AdventureType adventureType,
        SkillLevel skillLevel
) {
}