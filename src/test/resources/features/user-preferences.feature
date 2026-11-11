Feature: User preferences

  Scenario: A user can update their language and currency
    Given user "preferences-user" is registered with password "correct-horse-123"
    When user "preferences-user" logs in with password "correct-horse-123"
    Then response status is 200
    When user "preferences-user" updates preferences to language "fr" and currency "USD"
    Then response status is 200
    And response preferences are language "fr" and currency "USD"

  Scenario: Updating preferences requires authentication
    When an unauthenticated client requests the gigs list
    Then response status is 401

  Scenario: Reading preferences requires authentication
    When an unauthenticated client requests preferences
    Then response status is 401

  Scenario: Updating preferences requires authentication
    When an unauthenticated client updates preferences
    Then response status is 401
