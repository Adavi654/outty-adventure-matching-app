package com.outty.backend.profile.entity.enums;

import java.util.Locale;

public final class GenderParser {

    private GenderParser() {
    }

    public static Gender parse(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }

        String normalized = value.trim()
                .toUpperCase(Locale.ROOT)
                .replace("-", "")
                .replace(" ", "");

        return switch (normalized) {
            case "MALE" -> Gender.MALE;
            case "FEMALE" -> Gender.FEMALE;
            case "NONBINARY" -> Gender.NONBINARY;
            case "PREFERNOT", "PREFERNOTTOSAY", "PREFERSNOTTOSAY" -> Gender.PREFERNOT;
            default -> null;
        };
    }
}
