package org.example.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.dto.LocationDto;
import org.example.model.User;
import org.example.repository.GroupRepository;
import org.example.repository.LocationRepository;
import org.example.repository.MeetupRepository;
import org.example.repository.UserRepository;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.example.Constants.*;
import static org.example.controllers.Utils.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class LocationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    public UserRepository userRepository;

    @Autowired
    public LocationRepository locationRepository;

    @Autowired
    public MeetupRepository meetupRepository;

    @Autowired
    public GroupRepository groupRepository;

    @Test
    void shouldCreateLocationEndpoint() throws Exception {
        // preconditions
        User user = new User(USERNAME, PASSWORD);
        userRepository.save(user);

        // test
        String token = getAuthToken(mockMvc, USERNAME, PASSWORD);

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
