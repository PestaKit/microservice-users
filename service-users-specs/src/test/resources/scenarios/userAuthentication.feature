Feature: user Authentication

  Background:
    Given there is a User and Team Server

  Scenario: authenticate user with username
    Given I have a user named guy payload
    When I POST it to the /users endpoint
    Then I receive a 201 status code
    Given I have it credential payload using username
    When I POST it credential to the /auth endpoint
    Then I receive a 200 status code

  Scenario: authenticate user with email
    Given I have a user named Antoine payload
    When I POST it to the /users endpoint
    Then I receive a 201 status code
    Given I have it credential payload using email
    When I POST it credential to the /auth endpoint
    Then I receive a 200 status code


  Scenario: cannot authenticate non-existant user
    Given I have a user named coucou payload
    When I GET the user with the username coucou
    Then I receive a 404 status code
    Given I have it credential payload using username
    When I POST it credential to the /auth endpoint
    Then I receive a 401 status code

  Scenario: cannot authenticate user with wrong password
    Given I have a user named andre payload
    When I POST it to the /users endpoint
    Then I receive a 201 status code
    Given I have it credential payload using wrongpassword
    When I POST it credential to the /auth endpoint
    Then I receive a 401 status code

    
