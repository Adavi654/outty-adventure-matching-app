package com.outty.backend.profile.entity.enums;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.outty.backend.matching.dto.response.PotentialMatchResponse;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class GenderParserTest {

    private final ObjectMapper objectMapper = new ObjectMapper()
            .findAndRegisterModules();

    @Test
    void shouldParseDisplayValuesWithoutUsingValueOf() {
        assertEquals(Gender.MALE, GenderParser.parse("Male"));
        assertEquals(Gender.FEMALE, GenderParser.parse("Female"));
        assertEquals(Gender.NONBINARY, GenderParser.parse("Non-binary"));
        assertEquals(Gender.PREFERNOT, GenderParser.parse("Prefer not to say"));
    }

    @Test
    void shouldParseEnumNames() {
        assertEquals(Gender.MALE, GenderParser.parse("MALE"));
        assertEquals(Gender.FEMALE, GenderParser.parse("FEMALE"));
        assertEquals(Gender.NONBINARY, GenderParser.parse("NONBINARY"));
        assertEquals(Gender.PREFERNOT, GenderParser.parse("PREFERNOT"));
    }

    @Test
    void shouldReturnNullForBlankOrUnknownValues() {
        assertNull(GenderParser.parse(null));
        assertNull(GenderParser.parse(""));
        assertNull(GenderParser.parse("   "));
        assertNull(GenderParser.parse("Unknown"));
    }

    @Test
    void shouldDeserializeDisplayGenderInPotentialMatchResponse() throws Exception {
        String json = """
                {
                  "userId": 1,
                  "firstName": "Jordan",
                  "photoUrl": null,
                  "city": "Atlanta",
                  "state": "Georgia",
                  "country": "United States",
                  "gender": "Male",
                  "birthDate": null,
                  "bio": null,
                  "interestedIn": "BOTH",
                  "relationshipGoal": "FRIENDSHIPS",
                  "adventures": [],
                  "distanceMiles": 20,
                  "demoData": true
                }
                """;

        PotentialMatchResponse response = objectMapper.readValue(json, PotentialMatchResponse.class);

        assertEquals(Gender.MALE, response.gender());
    }

    @Test
    void fromJsonShouldDelegateToParser() {
        assertEquals(Gender.MALE, Gender.fromJson("Male"));
        assertEquals(List.of(Gender.MALE), List.of(Gender.fromJson("Male")));
    }
}
