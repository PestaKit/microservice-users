Feature: User Retrieval

  Background:
    Given there is a User and Team Server

  Scenario: List users
    When I GET the /users endpoint
    Then I receive a 200 status code
    And the response is a list containing 4 users
    And one user has the following attributes:
      | Username | Email         | Password | FirstName | LastName | DisplayName |
      | userjohn | john@john.com | passjohn | firstjohn | lastjohn | displayjohn |
    And one user has the following attributes:
      | Username   | Email             | Password   | FirstName   | LastName   | DisplayName   |
      | userGeorge | George@George.com | passGeorge | firstGeorge | lastGeorge | displayGeorge |

  Scenario: Get a valid user
    When I GET the user with the username userjohn
    Then I receive a 200 status code
    And the response contains a user
    And the user has the following atttributes:
      | Username | Email         | Password | FirstName | LastName | DisplayName |
      | userjohn | john@john.com | passjohn | firstjohn | lastjohn | displayjohn |

  Scenario: Get a unvalid user
    When I GET the user with the username userFake
    Then I receive a 404 status code

  Scenario: Get a valid user (case insensitive)
    When I GET the user with the username UsErJoHn
    Then I receive a 200 status code
    And the response contains a user
    And the user has the following atttributes:
      | Username | Email         | Password | FirstName | LastName | DisplayName |
      | userjohn | john@john.com | passjohn | firstjohn | lastjohn | displayjohn |