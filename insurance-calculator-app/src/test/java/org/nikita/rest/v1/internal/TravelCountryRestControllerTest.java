package org.nikita.rest.v1.internal;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.nikita.api.dto.CountryDto;
import org.nikita.core.service.CountryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser(username = "testuser", roles = {"USER"})
class TravelCountryRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CountryService countryService;

    @Autowired
    private ObjectMapper objectMapper;

    private CountryDto usaCountry;
    private CountryDto spainCountry;

    @BeforeEach
    void setUp() {
        usaCountry = new CountryDto("USA", "United States");
        spainCountry = new CountryDto("SPAIN", "Spain");
    }

    @Test
    void testGetAllCountriesSuccess() throws Exception {
        when(countryService.getAllCountries()).thenReturn(List.of(usaCountry, spainCountry));

        mockMvc.perform(get("/api/countries"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("success"))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data.length()").value(2))
                .andExpect(jsonPath("$.data[0].code").value("USA"))
                .andExpect(jsonPath("$.data[1].code").value("SPAIN"));

        verify(countryService, times(1)).getAllCountries();
    }

    @Test
    void testGetCountryByCodeSuccess() throws Exception {
        when(countryService.getCountryByCode("USA")).thenReturn(usaCountry);

        mockMvc.perform(get("/api/countries/USA"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("success"))
                .andExpect(jsonPath("$.data.code").value("USA"))
                .andExpect(jsonPath("$.data.name").value("United States"));

        verify(countryService, times(1)).getCountryByCode("USA");
    }

    @Test
    void testGetCountryByCodeNotFound() throws Exception {
        when(countryService.getCountryByCode("UNKNOWN")).thenThrow(new EntityNotFoundException("Country not found"));

        mockMvc.perform(get("/api/countries/UNKNOWN"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("error"))
                .andExpect(jsonPath("$.data").value("Country not found"));

        verify(countryService, times(1)).getCountryByCode("UNKNOWN");
    }
}

