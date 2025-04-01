package org.example.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.dto.GroupDto;
import org.example.dto.LocationDto;
import org.example.dto.MeetupDto;
import org.example.dto.UserDto;
import org.example.mapper.GroupMapper;
import org.example.mapper.LocationMapper;
import org.example.mapper.MeetupMapper;
import org.example.mapper.UserMapper;
import org.example.model.Group;
import org.example.model.Location;
import org.example.model.Meetup;
import org.example.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.example.Constants.*;
import static org.example.controllers.Utils.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.junit.jupiter.api.BeforeEach;

import java.time.Instant;

@SpringBootTest
@AutoConfigureMockMvc
public class MeetupControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private Utils testUtils;

    LocationDto locationDto;
    UserDto userDto;
    MeetupDto meetupDto;
    GroupDto groupDto;

    @BeforeEach
    void setup() {
        testUtils.cleanDatabase();

        Location location = testUtils.createLocation(LOCATION_CITY, LOCATION_ADDRESS);
        locationDto = LocationMapper.toDto(location);
        User user = testUtils.createUser(USERNAME, PASSWORD);
        userDto = UserMapper.toDto(user);
        Group savedGroup = testUtils.createGroup(GROUP_NAME, user);
        groupDto = GroupMapper.toDto(savedGroup);
        Meetup meetup = testUtils.createMeetup(MEETUP_TITLE, Instant.now(), user, savedGroup, location);
        meetupDto = MeetupMapper.toDto(meetup);
    }

    @Test
    void shouldCreateMeetupEndpoint() throws Exception {
        String token = getAuthToken(mockMvc, USERNAME, PASSWORD);

        MeetupDto request = new MeetupDto();
        request.setTitle(MEETUP_TITLE);
        request.setDescription(MEETUP_DESCR);
        request.setDateTime(Instant.now());
        request.setOrganizer(userDto);
        request.setLocation(locationDto);
        request.setGroup(groupDto);

        mockMvc.perform(post(CREATE_MEETUP)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.title").value(MEETUP_TITLE))
                .andExpect(jsonPath("$.location.id").value(locationDto.getId()))
                .andExpect(jsonPath("$.organizer.id").value(userDto.getId()))
                .andReturn();
    }

    @Test
    void shouldGetMeetupEndpoint() throws Exception {
        String token = Utils.getAuthToken(mockMvc, USERNAME, PASSWORD);

        mockMvc.perform(get(GET_MEETUP, meetupDto.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value(MEETUP_TITLE))
                .andExpect(jsonPath("$.location.address").value(LOCATION_ADDRESS))
                .andExpect(jsonPath("$.location.city").value(LOCATION_CITY))
                .andExpect(jsonPath("$.id").value(meetupDto.getId()));
    }

    @Test
    void shouldGetMeetupsEndpoint() throws Exception {}
    @Test
    void shouldUpdateMeetupEndpoint() throws Exception {}
    @Test
    void shouldRemoveMeetupEndpoint() throws Exception {}
    @Test
    void shouldAttentMeetupEndpoint() throws Exception {}
    @Test
    void shouldUnattentMeetupEndpoint() throws Exception {}
    @Test
    void shouldGetMeetupAttendeesEndpoint() throws Exception {}

}
