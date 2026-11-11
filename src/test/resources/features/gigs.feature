Feature: Gig management and ownership

  Scenario: A user can create, read, update and delete their own gig
    Given user "gig-owner" is registered with password "correct-horse-123"
    When user "gig-owner" logs in with password "correct-horse-123"
    Then response status is 200
    When user "gig-owner" creates a gig
    Then response status is 201
    When user "gig-owner" reads their gig
    Then response status is 200
    When user "gig-owner" updates their gig venue to "Le Zénith"
    Then response status is 200
    When user "gig-owner" reads their gig
    Then response status is 200
    And response contains:
      """yml
      venue: Le Zénith
      """
    When user "gig-owner" deletes their gig
    Then response status is 204
    Then the gig created by "gig-owner" no longer exists

  Scenario: A user cannot update another user's gig
    Given user "gig-owner-a" is registered with password "correct-horse-123"
    And user "gig-owner-b" is registered with password "correct-horse-123"
    When user "gig-owner-a" logs in with password "correct-horse-123"
    Then response status is 200
    When user "gig-owner-a" creates a gig
    Then response status is 201
    When user "gig-owner-b" logs in with password "correct-horse-123"
    Then response status is 200
    When user "gig-owner-b" tries to update "gig-owner-a"'s gig to venue "Hijacked venue"
    Then response status is 404
    And the gig created by "gig-owner-a" still has venue "Le Splendid"
    And the gig created by "gig-owner-a" still belongs to that user

  Scenario: A user cannot read another user's gig
    Given user "reader-owner" is registered with password "correct-horse-123"
    And user "reader-attacker" is registered with password "correct-horse-123"
    When user "reader-owner" logs in with password "correct-horse-123"
    Then response status is 200
    When user "reader-owner" creates a gig
    Then response status is 201
    When user "reader-attacker" logs in with password "correct-horse-123"
    Then response status is 200
    When user "reader-attacker" tries to read "reader-owner"'s gig
    Then response status is 404

  Scenario: A user cannot delete another user's gig
    Given user "delete-owner" is registered with password "correct-horse-123"
    And user "delete-attacker" is registered with password "correct-horse-123"
    When user "delete-owner" logs in with password "correct-horse-123"
    Then response status is 200
    When user "delete-owner" creates a gig
    Then response status is 201
    When user "delete-attacker" logs in with password "correct-horse-123"
    Then response status is 200
    When user "delete-attacker" tries to delete "delete-owner"'s gig
    Then response status is 404
    And the gig created by "delete-owner" still has venue "Le Splendid"

  Scenario: The gigs endpoint requires authentication
    When an unauthenticated client requests the gigs list
    Then response status is 401
