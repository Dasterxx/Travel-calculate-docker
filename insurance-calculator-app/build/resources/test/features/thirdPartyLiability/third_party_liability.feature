Feature: Travel Medical Risk Premium Calculation

  Scenario: Calculate travel medical premium for a person
    Given a request with agreement dates from "2024-01-01T00:00" to "2024-01-03T00:00"
    And a person with birth date "1990-01-01"
    And the request has countries to visit "SPAIN"
    And the request has insurance limit "10000"
    And the day count calculator returns 2 days
    And the country default day rate is "50.00"
    And the country coefficient is "1.1"
    And the insurance limit coefficient is "1.0"
    And the age coefficient for age 34 is "1.3"
    When the premium is calculated
    Then the premium should be "286.00"
