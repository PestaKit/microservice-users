Feature: Creation of fruits

  Background:
    Given there is a Fruits server

  Scenario: create a fruit
    Given I have a fruit payload
    When I POST it to the /fruits endpoint
    Then I receive a 201 status code