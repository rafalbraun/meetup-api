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
import org.example.repository.UserRepository;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.example.Constants.*;
import static org.example.controllers.Utils.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LocationRepository locationRepository;

    @Autowired
    private GroupRepository groupRepository;

    @Test
    void shouldUserAttendMeetupEndpoint() throws Exception {

        // preconditions
        User user = new User(USERNAME, PASSWORD);
        Location location = new Location(LOCATION_CITY, LOCATION_ADDRESS);
        User owner = new User(OWNER, PASSWORD);
        Group savedGroup = new Group(GROUP_NAME, owner);
        locationRepository.save(location);
        userRepository.save(owner);
        userRepository.save(user);
        groupRepository.save(savedGroup);
        LocationDto locationDto = LocationMapper.toDto(location);
        UserDto ownerDto = UserMapper.toDto(owner);
        GroupDto groupDto = GroupMapper.toDto(savedGroup);

        // test
        String token = getAuthToken(mockMvc, USERNAME, PASSWORD);

        MeetupDto request = createMeetupRequest(MEETUP_TITLE, MEETUP_DESCR, ownerDto, locationDto, groupDto);

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

        mockMvc.perform(get(ATTEND_MEETUP, meetup.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andReturn();

        mockMvc.perform(get(JOINED_MEETUPS)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andReturn();

        mockMvc.perform(get(UNATTEND_MEETUP, meetup.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andReturn();

        mockMvc.perform(get(JOINED_MEETUPS)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0))
                .andReturn();
    }

    @Test
    void shouldUserJoinGroupEndpoint() throws Exception {

        // preconditions
        User user = new User(USERNAME, PASSWORD);
        Location location = new Location(LOCATION_CITY, LOCATION_ADDRESS);
        User owner = new User(OWNER, PASSWORD);
        locationRepository.save(location);
        userRepository.save(owner);
        userRepository.save(user);
        UserDto ownerDto = UserMapper.toDto(owner);

        // test
        String token = getAuthToken(mockMvc, USERNAME, PASSWORD);

        GroupDto request = createGroupRequest(GROUP_NAME, ownerDto);

        MvcResult result = mockMvc.perform(post(CREATE_GROUP)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").value(GROUP_NAME))
                .andExpect(jsonPath("$.owner.id").value(ownerDto.getId()))
                .andReturn();

        String content = result.getResponse().getContentAsString();

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        GroupDto group = objectMapper.readValue(content, GroupDto.class);

        mockMvc.perform(get(JOIN_GROUP, group.getId())
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

        mockMvc.perform(get(LEAVE_GROUP, group.getId())
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
