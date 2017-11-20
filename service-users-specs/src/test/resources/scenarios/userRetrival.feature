Feature: User Retrieval

  Background:
    Given there is a User and Team Server

  Scenario: List users
    Given I have a user named John payload
    When I POST it to the /users endpoint
    Then I receive a 201 status code
    Given I have a user named George payload
    When I POST it to the /users endpoint
    Then I receive a 201 status code
    When I GET the /users endpoint
    Then I receive a 200 status code
    And the response is a list containing at least 2 users


  Scenario: Get a valid user
    Given I have a user named john payload
    When I POST it to the /users endpoint
    Then I receive a 201 status code
    When I GET the user with the username john
    Then I receive a 200 status code
    And the response contains a user


  Scenario: Get an unvalid user
    When I GET the user with the username johhn
    Then I receive a 404 status code


  Scenario: Get a valid user (case insensitive)
    Given I have a user named john payload
    When I POST it to the /users endpoint
    Then I receive a 201 status code
    When I GET the user with the username JoHn
    Then I receive a 200 status code
    And the response contains a user
