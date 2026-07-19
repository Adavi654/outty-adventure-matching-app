Feature: Connect Social Media
  As a registered Outty user
  I want to link my social media profiles
  So that others can find me on Instagram, Facebook, and X

  Scenario: Update Instagram and clear Facebook
    Given a registered user has a profile
    When the user updates the profile with a valid Instagram URL and clears Facebook
    Then the request succeeds
    And retrieving the profile returns the Instagram URL
    And Facebook is null
    And the Instagram link uses HTTPS
