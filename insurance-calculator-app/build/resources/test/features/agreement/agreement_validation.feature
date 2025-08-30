Feature: Agreement validation and saving

  Scenario: Prevent saving duplicate persons in agreement
    Given an agreement with duplicate persons
    When I try to save the agreement
    Then the save should fail with a validation error

  Scenario: Save valid agreement with unique persons
    Given an agreement with unique persons
    When I try to save the agreement
    Then the agreement should be saved successfully
