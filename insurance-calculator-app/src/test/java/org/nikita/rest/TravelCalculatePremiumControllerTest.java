package org.nikita.rest;


import org.junit.jupiter.api.extension.ExtendWith;
import org.nikita.core.blacklist.service.BlackListPersonCheckService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static uk.org.webcompere.modelassert.json.JsonAssertions.assertJson;


@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@Profile("test")
@WithMockUser(username = "test@example.com", roles = {"USER"})
abstract class TravelCalculatePremiumControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private BlackListPersonCheckService blackListPersonCheckService;

    @Autowired
    private JsonFileReader jsonFileReader;

    void executeAndCompare(String jsonRequestFilePath,
                           String jsonResponseFilePath) throws Exception {
        String jsonRequest = jsonFileReader.readJsonFromFile(jsonRequestFilePath);

        MvcResult result = mockMvc.perform(post("/insurance/travel/api/v1/")
                        .content(jsonRequest)
                        .header(HttpHeaders.CONTENT_TYPE,
                                MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andReturn();

        String responseBodyContent = result.getResponse().getContentAsString();

        String jsonResponse = jsonFileReader.readJsonFromFile(jsonResponseFilePath);


        assertJson(responseBodyContent).where().keysInAnyOrder().arrayInAnyOrder().isEqualTo(jsonResponse);
    }
}
