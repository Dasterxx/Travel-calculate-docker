package org.nikita.web.v3;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class TravelInsuranceRestControllerV3Test {

    @Autowired
    private MockMvc mockMvc;

    private String jsonRequest = """
            {
                "agreement": {
                    "agreementDateFrom": "2028-03-15T00:00:00",
                    "agreementDateTo": "2028-03-31T23:59:59",
                    "countriesToVisit": ["USA", "Spain"],
                    "insuranceLimit": 10000,
                    "selectedRisks": ["TRAVEL_MEDICAL"],
                    "persons": [
                        {
                            "firstName": "Vasja",
                            "lastName": "Pupkin",
                            "birthDate": "1950-03-15",
                            "email": "testuser@gmail.com",
                            "gender": "male",
                            "password": "32321323213213",
                            "roles": ["ROLE_USER"],
                            "personCode": "A-12345"
                        }
                    ]
                }
            }
            """;

    @Test
    @WithMockUser(username = "testuser@example", roles = {"USER"})
    public void testSecuredEndpointWithValidToken() throws Exception {
        mockMvc.perform(post("/insurance/travel/web/v3")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Success!"));
    }


}

