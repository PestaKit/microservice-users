Feature: public key retrival

  Background:
    Given there is a User and Team Server

  Scenario: get a valid public key
    When I GET the /publicKey endpoint
    Then I receive a 200 status code
    And the response contains a public key