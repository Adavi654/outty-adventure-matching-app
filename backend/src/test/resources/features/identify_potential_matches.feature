Feature: Identify potential matches
  As a registered Outty user
  I want to view potential matching profiles
  So that I can discover people I may want to connect with

  Scenario: Return only compatible adventure matches
    Given a registered user has a complete profile
    And the user has relationship goal "FRIENDSHIPS"
    And the user has adventure "HIKING" at skill level "INTERMEDIATE"
    When the user requests potential matches
    Then the request should succeed
    And the requesting user should not be included in the results
    And every returned match should share at least one adventure with the user
    And every returned match should have a compatible relationship goal
