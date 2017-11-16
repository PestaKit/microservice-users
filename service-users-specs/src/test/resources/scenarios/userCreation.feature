Feature: user creation

  Background:
    Given there is a User and Team Server

  Scenario: create a user
    Given I have a user payload
    When I POST it to the /users endpoint
    Then I receive a 201 status code

  Scenario: cannot create twice the same user
    Given I have a user named Johny payload
    When I POST it to the /users endpoint
    Then I receive a 201 status code
    When I POST it to the /users endpoint
    Then I receive a 403 status code

  Scenario: create two users with capitalized username
    Given I have a user named George payload
    When I POST it to the /users endpoint
    Then I receive a 201 status code
    Given I have a user named george payload
    When I POST it to the /users endpoint
    Then I receive a 403 status code

  Scenario: user endpoint given after creation
    Given I have a user named Alfred payload
    When I POST it to the /users endpoint
    Then I receive a 201 status code
    Then I receive an endpoint to the user

  Scenario: cannot create user containing illegal chars
    Given I have a user named d/a payload
    Then I receive a 422 status code
