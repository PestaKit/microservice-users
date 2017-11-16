Feature: User Retrieval

  Background:
    Given There is an API server

  @run_me
  Scenario: Retrieve an user with a valid id
    Given I have an user with id=1
    When I POST it to the /user endpoint
    Then I receive a 200 status code

  @run_me
  Scenario: Retrieve an user with an invalid id
    Given I have an user with id=999
    When I POST it to the /user endpoint
    Then I receive a 404 status code