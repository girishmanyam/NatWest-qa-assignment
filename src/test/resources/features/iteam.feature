Feature: Manage items through REST API
  As a user of the system
  I want to be able to create, retrieve, list, and delete items
  So that I can manage my items through the API

  Scenario: Verify an item can be created
    Given a "Samsung Galaxy Book3 Pro" item is created
    And is a "Intel Core i7" CPU model
    And has a price of "1699.99"
    When the request to add the item is made
    Then a "addItem" service returns successful response code
    And the "addItem" response should contain a item details