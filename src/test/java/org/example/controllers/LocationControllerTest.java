package org.example.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.dto.LocationDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.example.Constants.*;
import static org.example.controllers.Utils.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@AutoConfigureMockMvc
public class LocationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private Utils testUtils;

    @BeforeEach
    void setup() {
        testUtils.cleanDatabase();
        testUtils.createUser(USERNAME, PASSWORD);
    }

    @Test
    void shouldCreateLocationEndpoint() throws Exception {
        String token = Utils.getAuthToken(mockMvc, USERNAME, PASSWORD);

        LocationDto request = new LocationDto();
        request.setCity(LOCATION_CITY);
        request.setAddress(LOCATION_ADDRESS);

        MvcResult result = mockMvc.perform(post(GET_LOCATION)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.city").value(LOCATION_CITY))
                .andExpect(jsonPath("$.address").value(LOCATION_ADDRESS))
                .andReturn();
    }

}
