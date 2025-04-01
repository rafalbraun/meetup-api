package org.example.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.dto.MeetupDto;
import org.example.dto.UserDto;
import org.example.model.Group;
import org.example.model.Location;
import org.example.model.Meetup;
import org.example.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.time.Instant;

import static org.example.Constants.*;
import static org.example.controllers.Utils.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private MockMvcTestHelper helper;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private Utils testUtils;

    private Long meetupId, groupId, userId;

    @BeforeEach
    void setUp() {
        testUtils.cleanDatabase();

        Location location = testUtils.createLocation(LOCATION_CITY, LOCATION_ADDRESS);
        User user = testUtils.createUser(USERNAME, PASSWORD);
        Group savedGroup = testUtils.createGroup(GROUP_NAME, user);
        Meetup savedMeetup = testUtils.createMeetup(MEETUP_TITLE, Instant.now(), user, savedGroup, location);

        groupId = savedGroup.getId();
        meetupId = savedMeetup.getId();
        userId = user.getId();

        helper = new MockMvcTestHelper(mockMvc);
    }

    @Test
    void shouldUserUpdateEndpoint() throws Exception {
        String token = getAuthToken(mockMvc, USERNAME, PASSWORD);

        mockMvc.perform(get(GET_USER, userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.bio").isEmpty())
                .andReturn();

        UserDto request = new UserDto();
        request.setBio(TEST_BIO);

        mockMvc.perform(put(UPDATE_USER, userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andReturn();

        mockMvc.perform(get(GET_USER, userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.bio").value(TEST_BIO))
                .andReturn();

    }

    @Test
    void shouldUserAttendMeetupEndpoint() throws Exception {
        String token = getAuthToken(mockMvc, USERNAME, PASSWORD);

        helper.performAuthGet(token, ATTEND_MEETUP, meetupId)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andReturn();

        helper.performAuthGet(token, JOINED_MEETUPS)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andReturn();

        helper.performAuthGet(token, UNATTEND_MEETUP, meetupId)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andReturn();

        helper.performAuthGet(token, JOINED_MEETUPS)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0))
                .andReturn();
    }

    @Test
    void shouldUserJoinGroupEndpoint() throws Exception {
        String token = getAuthToken(mockMvc, USERNAME, PASSWORD);

        mockMvc.perform(get(JOIN_GROUP, groupId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andReturn();

        mockMvc.perform(get(JOINED_GROUPS)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andReturn();

        mockMvc.perform(get(LEAVE_GROUP, groupId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andReturn();

        mockMvc.perform(get(JOINED_GROUPS)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0))
                .andReturn();
    }


}
