package com.outty.backend.matching.bdd;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.outty.backend.auth.entity.User;
import com.outty.backend.auth.entity.enums.Role;
import com.outty.backend.auth.repository.UserRepository;
import com.outty.backend.profile.entity.Profile;
import com.outty.backend.profile.entity.ProfileAdventure;
import com.outty.backend.profile.entity.enums.AdventureType;
import com.outty.backend.profile.entity.enums.Gender;
import com.outty.backend.profile.entity.enums.InterestedIn;
import com.outty.backend.profile.entity.enums.RelationshipGoal;
import com.outty.backend.profile.entity.enums.SkillLevel;
import com.outty.backend.profile.repository.ProfileRepository;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class PotentialMatchSteps {

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProfileRepository profileRepository;

    private final ObjectMapper objectMapper = new ObjectMapper()
            .findAndRegisterModules();

    private MockMvc mockMvc;
    private ResultActions potentialMatchesResult;
    private Long testUserId;
    private RelationshipGoal requesterRelationshipGoal;
    private Set<String> requesterAdventureTypes;
    private List<Map<String, Object>> matchesResponse;

    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @After
    public void cleanUp() {
        if (testUserId == null) {
            return;
        }

        profileRepository.findByUserId(testUserId)
                .ifPresent(profileRepository::delete);
        userRepository.findById(testUserId)
                .ifPresent(userRepository::delete);
    }

    @Given("a registered user has a complete profile")
    public void aRegisteredUserHasACompleteProfile() {
        User user = User.builder()
                .firstName("Match")
                .lastName("BDD")
                .email("match-bdd-" + UUID.randomUUID() + "@example.com")
                .password("not-used")
                .role(Role.USER)
                .enabled(true)
                .build();

        User savedUser = userRepository.save(user);
        testUserId = savedUser.getId();

        Profile profile = Profile.builder()
                .user(savedUser)
                .city("Atlanta")
                .state("Georgia")
                .country("United States")
                .gender(Gender.PREFERNOT)
                .birthDate(LocalDate.of(1995, 5, 15))
                .bio("BDD profile for identifying potential matches.")
                .interestedIn(InterestedIn.BOTH)
                .relationshipGoal(RelationshipGoal.FRIENDSHIPS)
                .adventures(new ArrayList<>())
                .build();

        profileRepository.save(profile);
    }

    @Given("the user has relationship goal {string}")
    public void theUserHasRelationshipGoal(String relationshipGoal) {
        Profile profile = profileRepository.findByUserId(testUserId)
                .orElseThrow();

        requesterRelationshipGoal = RelationshipGoal.valueOf(relationshipGoal);
        profile.setRelationshipGoal(requesterRelationshipGoal);
        profileRepository.save(profile);
    }

    @Given("the user has adventure {string} at skill level {string}")
    public void theUserHasAdventureAtSkillLevel(String adventureType, String skillLevel) {
        Profile profile = profileRepository.findByUserId(testUserId)
                .orElseThrow();

        AdventureType type = AdventureType.valueOf(adventureType);
        SkillLevel level = SkillLevel.valueOf(skillLevel);

        requesterAdventureTypes = Set.of(adventureType);

        profile.setAdventures(new ArrayList<>(List.of(
                ProfileAdventure.builder()
                        .profile(profile)
                        .adventureType(type)
                        .skillLevel(level)
                        .build()
        )));
        profileRepository.save(profile);
    }

    @When("the user requests potential matches")
    public void theUserRequestsPotentialMatches() throws Exception {
        potentialMatchesResult = mockMvc.perform(
                get("/api/v1/users/{userId}/potential-matches", testUserId)
        );
    }

    @Then("the request should succeed")
    public void theRequestShouldSucceed() throws Exception {
        potentialMatchesResult.andExpect(status().isOk());

        String responseBody = potentialMatchesResult.andReturn()
                .getResponse()
                .getContentAsString();
        matchesResponse = objectMapper.readValue(
                responseBody,
                new TypeReference<>() {
                }
        );

        assertNotNull(matchesResponse);
        assertFalse(matchesResponse.isEmpty());
    }

    @Then("the requesting user should not be included in the results")
    public void theRequestingUserShouldNotBeIncludedInTheResults() {
        List<Number> returnedUserIds = matchesResponse.stream()
                .map(match -> (Number) match.get("userId"))
                .toList();

        assertFalse(returnedUserIds.contains(testUserId));
    }

    @Then("every returned match should share at least one adventure with the user")
    public void everyReturnedMatchShouldShareAtLeastOneAdventureWithTheUser() {
        for (Map<String, Object> match : matchesResponse) {
            @SuppressWarnings("unchecked")
            List<Map<String, Object>> adventures =
                    (List<Map<String, Object>>) match.get("adventures");

            assertNotNull(adventures);

            Set<String> candidateAdventureTypes = adventures.stream()
                    .map(adventure -> (String) adventure.get("adventureType"))
                    .collect(Collectors.toSet());

            assertTrue(
                    candidateAdventureTypes.stream().anyMatch(requesterAdventureTypes::contains),
                    "Expected shared adventure with requester for match " + match.get("userId")
            );
        }
    }

    @Then("every returned match should have a compatible relationship goal")
    public void everyReturnedMatchShouldHaveACompatibleRelationshipGoal() {
        for (Map<String, Object> match : matchesResponse) {
            String candidateGoal = (String) match.get("relationshipGoal");

            boolean compatible = candidateGoal.equals(requesterRelationshipGoal.name())
                    || candidateGoal.equals(RelationshipGoal.BOTH.name())
                    || requesterRelationshipGoal == RelationshipGoal.BOTH;

            assertTrue(
                    compatible,
                    "Expected compatible relationship goal for match " + match.get("userId")
            );
        }
    }
}
