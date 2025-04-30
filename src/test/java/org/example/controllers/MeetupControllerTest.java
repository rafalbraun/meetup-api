package org.example.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import org.example.dto.GroupDto;
import org.example.dto.LocationDto;
import org.example.dto.MeetupDto;
import org.example.dto.UserDto;
import org.example.mapper.GroupMapper;
import org.example.mapper.LocationMapper;
import org.example.mapper.UserMapper;
import org.example.model.Group;
import org.example.model.Location;
import org.example.model.User;
import org.example.repository.GroupRepository;
import org.example.repository.LocationRepository;
import org.example.repository.MeetupRepository;
import org.example.repository.UserRepository;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.example.Constants.*;
import static org.example.controllers.Utils.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.test.web.servlet.MvcResult;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class MeetupControllerTest {

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

    LocationDto locationDto;
    UserDto ownerDto;
    GroupDto groupDto;

    @BeforeEach
    void setup() {
        User user = new User(USERNAME, PASSWORD);
        Location location = new Location(LOCATION_CITY, LOCATION_ADDRESS);
        User owner = new User(OWNER, PASSWORD);
        Group savedGroup = new Group(GROUP_NAME, owner);

        locationRepository.save(location);
        userRepository.save(owner);
        userRepository.save(user);
        groupRepository.save(savedGroup);

        locationDto = LocationMapper.toDto(location);
        ownerDto = UserMapper.toDto(owner);
        groupDto = GroupMapper.toDto(savedGroup);
    }

    @Test
    void shouldCreateMeetupEndpoint() throws Exception {
        String token = getAuthToken(mockMvc, OWNER, PASSWORD);

        MeetupDto request = createMeetupRequest(MEETUP_TITLE_1, MEETUP_DESCR, ownerDto, locationDto, groupDto);

        MvcResult result = mockMvc.perform(post(CREATE_MEETUP)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.title").value(MEETUP_TITLE_1))
                .andExpect(jsonPath("$.description").value(MEETUP_DESCR))
                .andExpect(jsonPath("$.location.id").value(locationDto.getId()))
                .andExpect(jsonPath("$.organizer.id").value(ownerDto.getId()))
                .andReturn();

        String content = result.getResponse().getContentAsString();

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        MeetupDto meetup = objectMapper.readValue(content, MeetupDto.class);

        mockMvc.perform(get(GET_MEETUP, meetup.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value(meetup.getTitle()))
                .andExpect(jsonPath("$.location.address").value(meetup.getLocation().getAddress()))
                .andExpect(jsonPath("$.location.city").value(meetup.getLocation().getCity()))
                .andExpect(jsonPath("$.id").value(meetup.getId()));
    }

    @Test
    void shouldGetMeetupsEndpoint() throws Exception {
        String token = Utils.getAuthToken(mockMvc, OWNER, PASSWORD);

        for (String title : MEETUP_TITLES) {
            MeetupDto request = createMeetupRequest(title, "some descr", ownerDto, locationDto, groupDto);
            mockMvc.perform(post(CREATE_MEETUP)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request))
                            .header("Authorization", "Bearer " + token))
                    .andExpect(status().isCreated());
        }

        mockMvc.perform(get(GET_MEETUPS)
                    .contentType(MediaType.APPLICATION_JSON)
                    .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(3));
    }

    @Test
    void shouldUpdateMeetupEndpoint() throws Exception {
        String token = Utils.getAuthToken(mockMvc, OWNER, PASSWORD);

        MeetupDto request = createMeetupRequest(MEETUP_TITLE_1, MEETUP_DESCR, ownerDto, locationDto, groupDto);

        MvcResult result = mockMvc.perform(post(CREATE_MEETUP)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isCreated()).andReturn();

        String content = result.getResponse().getContentAsString();

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        MeetupDto meetup = objectMapper.readValue(content, MeetupDto.class);

        request.setTitle(MEETUP_TITLE_2);
        mockMvc.perform(put(UPDATE_MEETUP, meetup.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value(MEETUP_TITLE_2));

        mockMvc.perform(get(GET_MEETUP, meetup.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value(MEETUP_TITLE_2));
    }

    @Test
    void shouldRemoveMeetupEndpoint() throws Exception {
        String token = Utils.getAuthToken(mockMvc, OWNER, PASSWORD);

        MeetupDto request = createMeetupRequest(MEETUP_TITLE_1, MEETUP_DESCR, ownerDto, locationDto, groupDto);

        MvcResult result = mockMvc.perform(post(CREATE_MEETUP)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isCreated())
                .andReturn();

        String content = result.getResponse().getContentAsString();

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        MeetupDto meetup = objectMapper.readValue(content, MeetupDto.class);

        mockMvc.perform(delete(DELETE_MEETUP, meetup.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isNoContent());

        mockMvc.perform(get(GET_MEETUP, meetup.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldAttendMeetupEndpoint() throws Exception {
        String token_owner = Utils.getAuthToken(mockMvc, OWNER, PASSWORD);

        MeetupDto request = createMeetupRequest(MEETUP_TITLE_1, MEETUP_DESCR, ownerDto, locationDto, groupDto);

        MvcResult result = mockMvc.perform(post(CREATE_MEETUP)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .header("Authorization", "Bearer " + token_owner))
                .andExpect(status().isCreated())
                .andReturn();

        String content = result.getResponse().getContentAsString();

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        MeetupDto meetup = objectMapper.readValue(content, MeetupDto.class);

        String token_user = Utils.getAuthToken(mockMvc, USERNAME, PASSWORD);
        mockMvc.perform(get(ATTEND_MEETUP, meetup.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + token_user))
                .andExpect(status().isOk());

        mockMvc.perform(get(ALL_MEETUP_ATTENDEES, meetup.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + token_user))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));

        mockMvc.perform(get(UNATTEND_MEETUP, meetup.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + token_user))
                .andExpect(status().isOk());

        mockMvc.perform(get(ALL_MEETUP_ATTENDEES, meetup.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + token_user))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }

}
