Feature: User registration

  Scenario: A user can register with default preferences
    When user "alice" registers with password "correct-horse-123"
    Then response status is 201
    When user "alice" logs in with password "correct-horse-123"
    Then response status is 200
    And response contains a JWT
    When user "alice" requests their preferences
    Then response status is 200
    And response preferences are language "en" and currency "EUR"

  Scenario: A user can choose preferences during registration
    When user "bob" registers with password "correct-horse-123", language "fr" and currency "USD"
    Then response status is 201
    When user "bob" logs in with password "correct-horse-123"
    Then response status is 200
    When user "bob" requests their preferences
    Then response status is 200
    And response preferences are language "fr" and currency "USD"

  Scenario: A login cannot be registered twice
    Given user "duplicate" is registered with password "correct-horse-123"
    When user "duplicate" registers with password "another-password-123"
    Then response status is 409

  Scenario: Registration rejects a password that is too short
    When user "short-password" registers with password "short"
    Then response status is 400
