package com.outty.backend.auth.bdd;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.outty.backend.auth.dto.request.LoginRequest;
import com.outty.backend.auth.entity.User;
import com.outty.backend.auth.entity.enums.Role;
import com.outty.backend.auth.repository.UserRepository;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import io.cucumber.java.Before;

import io.cucumber.spring.CucumberContextConfiguration;

@CucumberContextConfiguration
@SpringBootTest
@ActiveProfiles("test")
public class LoginSteps {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext context;

    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ObjectMapper objectMapper;

    private ResultActions loginResult;

    @Given("a user exists with email {string} and password {string}")
    public void a_user_exists_with_email_and_password(String email, String password) {
        userRepository.findByEmail(email).ifPresent(userRepository::delete);
        
        User testUser = User.builder()
                .firstName("Dan")
                .lastName("Hiker")
                .email(email)
                .password(passwordEncoder.encode(password))
                .role(Role.USER)
                .enabled(true)
                .build();
        userRepository.save(testUser);
    }

    @When("the user attempts to log in with email {string} and password {string}")
    public void the_user_attempts_to_log_in_with_email_and_password(String email, String password) throws Exception {
        LoginRequest loginRequest = new LoginRequest(email, password);

        loginResult = mockMvc.perform(post("/api/v1/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)));
    }

    @Then("the login should be successful")
    public void the_login_should_be_successful() throws Exception {
        loginResult.andExpect(status().isOk());
    }

    @Then("a valid security token should be returned")
    public void a_valid_security_token_should_be_returned() throws Exception {
        loginResult.andExpect(jsonPath("$.token", notNullValue()));
    }

    @Then("the response should contain the email {string}")
    public void the_response_should_contain_the_email(String expectedEmail) throws Exception {
        loginResult.andExpect(jsonPath("$.email").value(expectedEmail));
    }

    @Then("the login should fail")
    public void the_login_should_fail() throws Exception {
        loginResult.andExpect(status().isUnauthorized()); 
    }

    @Then("an {string} error message should be returned")
    public void an_error_message_should_be_returned(String expectedErrorMessage) throws Exception {
        loginResult.andExpect(content().string(expectedErrorMessage));
    }
}
