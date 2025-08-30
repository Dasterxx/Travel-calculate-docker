package org.nikita.core.underwriting.calcs.activeRest;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.nikita.api.dto.AgreementDto;
import org.nikita.api.dto.PersonDto;
import org.nikita.core.underwriting.calcs.AgeCalculator;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class ActiveRestRiskSteps {

    @Mock
    private DayCountCalculatorActiveRest dayCountCalculator;

    @Mock
    private CountryDefaultDayRateActiveRest countryDefaultDayRateCalculator;

    @Mock
    private AgeCalcCoefficientActiveRest ageCalcCoefficient;

    @Mock
    private CountryCalcCoefficientActiveRest countryCalcCoefficient;

    @Mock
    private InsuranceLimitCalcCoefficientActiveRest insuranceLimitCalcCoefficient;

    @Mock
    private AgeCalculator ageCalculator;

    private TravelActiveRestRiskPremiumCalculator calculator;

    private AgreementDto request;
    private PersonDto person;
    private BigDecimal actualPremium;

    private AutoCloseable mocks;

    @Before
    public void setUp() {
        mocks = MockitoAnnotations.openMocks(this);
        calculator = new TravelActiveRestRiskPremiumCalculator(
                ageCalcCoefficient,
                countryCalcCoefficient,
                insuranceLimitCalcCoefficient,
                ageCalculator,
                dayCountCalculator,
                countryDefaultDayRateCalculator

        );
    }

    @After
    public void tearDown() throws Exception {
        if (mocks != null) {
            mocks.close();
        }
    }

    @Given("a request with agreement dates from {string} to {string}")
    public void a_request_with_agreement_dates_from_to(String from, String to) {
        request = new AgreementDto();
        request.setAgreementDateFrom(LocalDateTime.parse(from));
        request.setAgreementDateTo(LocalDateTime.parse(to));
    }

    @Given("a person with birth date {string}")
    public void a_person_with_birth_date(String birthDate) {
        person = new PersonDto();
        person.setBirthDate(LocalDate.parse(birthDate));
    }

    @Given("the request has countries to visit {string}")
    public void the_request_has_countries_to_visit(String countries) {
        request.setCountriesToVisit(List.of(countries));
    }

    @Given("the request has insurance limit {string}")
    public void the_request_has_insurance_limit(String insuranceLimit) {
        request.setInsuranceLimit(new BigDecimal(insuranceLimit));
    }

    @Given("the day count calculator returns {int} days")
    public void the_day_count_calculator_returns_days(int days) {
        when(dayCountCalculator.calculateDayCount(any())).thenReturn(BigDecimal.valueOf(days));
    }

    @Given("the country default day rate is {string}")
    public void the_country_default_day_rate_is(String rate) {
        when(countryDefaultDayRateCalculator.calculateCountryDefaultDayRate(any())).thenReturn(new BigDecimal(rate));
    }

    @Given("the country coefficient is {string}")
    public void the_country_coefficient_is(String coeff) {
        when(countryCalcCoefficient.getCountryCoefficient(any())).thenReturn(new BigDecimal(coeff));
    }

    @Given("the insurance limit coefficient is {string}")
    public void the_insurance_limit_coefficient_is(String coeff) {
        when(insuranceLimitCalcCoefficient.getInsuranceLimitCoefficient(any())).thenReturn(new BigDecimal(coeff));
    }

    @Given("the age coefficient for age {int} is {string}")
    public void the_age_coefficient_for_age_is(int age, String coeff) {
        when(ageCalculator.calculateAge(any())).thenReturn(age);
        when(ageCalcCoefficient.getAgeCoefficient(age)).thenReturn(new BigDecimal(coeff));
    }

    @When("the premium is calculated")
    public void the_premium_is_calculated() {
        actualPremium = calculator.calculatePremium(request, person);
    }

    @Then("the premium should be {string}")
    public void the_premium_should_be(String expectedPremium) {
        assertEquals(new BigDecimal(expectedPremium), actualPremium);
    }
}

