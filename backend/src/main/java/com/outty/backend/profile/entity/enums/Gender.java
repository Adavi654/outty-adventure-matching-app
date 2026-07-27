package com.outty.backend.profile.entity.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum Gender {
    MALE,
    FEMALE,
    NONBINARY,
    PREFERNOT;

    @JsonCreator
    public static Gender fromJson(String value) {
        return GenderParser.parse(value);
    }
}
