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

  Scenario: Ability to return an item
    Given a new item is created with the following details:
      | name                       | price   | cpuModel      | year | hardDiskSize |
      | Microsoft Surface Laptop 5 | 2799.50 | Intel Core i9 | 2021 | 1 TB         |
    When the user requests to get item details by itemId
    Then a "getItem" service returns successful response code
    And the item id value should match
    And the "getItem" response should contain a item details

  Scenario: Ability to return an multiple items
    Given a multiple items are created with the following details:
      | name                 | price   | cpuModel      | year | hardDiskSize |
      | Dell XPS 15          | 1899.99 | Intel Core i7 | 2022 | 512 GB       |
      | HP Spectre x360      | 1549.99 | Intel Core i5 | 2021 | 256 GB       |
      | Lenovo ThinkPad X1   | 2049.50 | Intel Core i9 | 2023 | 1 TB         |
      | Apple MacBook Air M2 | 1249.99 | Apple M2      | 2023 | 512 GB       |
    When the request to list all objects is made
    Then the response should contain more or equal to 4 items

  Scenario: Successfully delete an item
    Given a new item is created with the following details:
      | name        | price  | cpuModel      |year | hardDiskSize |
      | Dell XPS 15 | 899.99 | Intel Core i7 |2023 | 512 GB       |
    When the user requests to delete item by itemId
    Then item is deleted successfully

  Scenario: Delete an non-existing item
    Given I have a non-existing item
    When the user requests to delete item by itemId
    Then a "deleteItem" response should be 404 with error message


