package com.outty.backend.profile.bdd;

import com.outty.backend.auth.entity.User;
import com.outty.backend.auth.entity.enums.Role;
import com.outty.backend.auth.repository.UserRepository;
import com.outty.backend.profile.entity.Profile;
import com.outty.backend.profile.entity.enums.Gender;
import com.outty.backend.profile.entity.enums.InterestedIn;
import com.outty.backend.profile.entity.enums.RelationshipGoal;
import com.outty.backend.profile.repository.ProfileRepository;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDate;
import java.util.UUID;

import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.Matchers.startsWith;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class SocialMediaSteps {

    private static final String INSTAGRAM_URL = "https://instagram.com/outty_hiker";

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProfileRepository profileRepository;

    private MockMvc mockMvc;
    private ResultActions updateResult;
    private ResultActions getResult;
    private Long testUserId;

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

    @Given("a registered user has a profile")
    public void aRegisteredUserHasAProfile() {
        User user = User.builder()
                .firstName("Social")
                .lastName("BDD")
                .email("social-bdd-" + UUID.randomUUID() + "@example.com")
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
                .bio("BDD profile for connect social media.")
                .interestedIn(InterestedIn.BOTH)
                .relationshipGoal(RelationshipGoal.FRIENDSHIPS)
                .facebookUrl("https://facebook.com/outty.hiker")
                .build();

        profileRepository.save(profile);
    }

    @When("the user updates the profile with a valid Instagram URL and clears Facebook")
    public void theUserUpdatesTheProfileWithAValidInstagramUrlAndClearsFacebook()
            throws Exception {
        String requestBody = """
                {
                  "city": "Atlanta",
                  "state": "Georgia",
                  "country": "United States",
                  "gender": "PREFERNOT",
                  "birthDate": "1995-05-15",
                  "bio": "BDD profile for connect social media.",
                  "interestedIn": "BOTH",
                  "relationshipGoal": "FRIENDSHIPS",
                  "instagramUrl": "https://instagram.com/outty_hiker",
                  "facebookUrl": "",
                  "xUrl": null
                }
                """;

        updateResult = mockMvc.perform(
                put("/api/v1/users/{userId}/profile", testUserId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
        );
    }

    @Then("the request succeeds")
    public void theRequestSucceeds() throws Exception {
        updateResult.andExpect(status().isOk());
    }

    @Then("retrieving the profile returns the Instagram URL")
    public void retrievingTheProfileReturnsTheInstagramUrl() throws Exception {
        getResult = mockMvc.perform(get("/api/v1/users/{userId}/profile", testUserId));

        getResult.andExpect(status().isOk())
                .andExpect(jsonPath("$.instagramUrl").value(INSTAGRAM_URL));
    }

    @Then("Facebook is null")
    public void facebookIsNull() throws Exception {
        getResult.andExpect(jsonPath("$.facebookUrl").value(nullValue()));
    }

    @Then("the Instagram link uses HTTPS")
    public void theInstagramLinkUsesHttps() throws Exception {
        getResult.andExpect(jsonPath("$.instagramUrl", startsWith("https://")));
    }
}
