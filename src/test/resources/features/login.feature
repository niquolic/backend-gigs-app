Feature: User authentication

  Scenario: A registered user can log in
    Given user "login-success" is registered with password "correct-horse-123"
    When user "login-success" logs in with password "correct-horse-123"
    Then response status is 200
    And response contains a JWT

  Scenario: A wrong password is rejected
    Given user "wrong-password" is registered with password "correct-horse-123"
    When user "wrong-password" logs in with password "incorrect-password"
    Then response status is 404

  Scenario: An unknown login is rejected
    When user "unknown-user" logs in with password "correct-horse-123"
    Then response status is 404

  Scenario: A logged-out token cannot access protected endpoints
    Given user "logout-user" is registered with password "correct-horse-123"
    When user "logout-user" logs in with password "correct-horse-123"
    Then response status is 200
    When user "logout-user" logs out
    Then response status is 204
    When user "logout-user" requests the gigs list
    Then response status is 401

  Scenario: Protected endpoints reject requests without a token
    When an unauthenticated client requests the gigs list
    Then response status is 401
