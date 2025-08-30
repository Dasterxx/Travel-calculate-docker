Feature: Travel Cancellation Risk Premium Calculation

  Scenario: Calculate travel cancellation premium for a person
    Given a request with agreement dates from "2025-01-01T00:00" to "2025-01-10T00:00"
    And a person with birth date "1980-01-01"
    And the request has countries to visit "LATVIA"
    And the request has insurance limit "10000"
    And the day count calculator returns 9 days
    And the country default day rate is "50.00"
    And the country coefficient is "1.1"
    And the insurance limit coefficient is "1.0"
    And the age coefficient for age 45 is "1.5"
    And the travel cost coefficient is "1.0"
    And the country safety rating coefficient is "1.0"
    When the premium is calculated
    Then the premium should be "891.00"
