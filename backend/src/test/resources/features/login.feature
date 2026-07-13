Feature: User Login
  As an outdoor enthusiast
  I want to log into the Outty adventure app
  So that I can find adventure partners match my skill level

  Background:
    Given a user exists with email "hiker_dan@example.com" and password "TrailBlazer2026!"

  Scenario: Successful login with valid credentials
    When the user attempts to log in with email "hiker_dan@example.com" and password "TrailBlazer2026!"
    Then the login should be successful
    And a valid security token should be returned
    And the response should contain the email "hiker_dan@example.com"

  Scenario: Unsuccessful login with incorrect password
    When the user attempts to log in with email "hiker_dan@example.com" and password "WrongPassword"
    Then the login should fail
    And an "Invalid email or password" error message should be returned