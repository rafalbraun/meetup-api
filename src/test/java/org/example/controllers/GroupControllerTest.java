package org.example.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.example.dto.GroupDto;
import org.example.dto.LocationDto;
import org.example.dto.MeetupDto;
import org.example.dto.UserDto;
import org.example.exceptions.ResourceNotFoundException;
import org.example.mapper.GroupMapper;
import org.example.mapper.LocationMapper;
import org.example.mapper.UserMapper;
import org.example.model.Group;
import org.example.model.Location;
import org.example.model.User;
import org.example.repository.LocationRepository;
import org.example.repository.MeetupRepository;
import org.example.repository.UserRepository;
import org.example.repository.GroupRepository;
import org.example.service.GroupService;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.example.Constants.*;
import static org.example.controllers.Utils.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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
public class GroupControllerTest {

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

    @Autowired
    public GroupService groupService;

    LocationDto locationDto;
    UserDto ownerDto;
    GroupDto groupDto;

    @BeforeEach
    void setup() {
        User owner = new User(OWNER, PASSWORD);
        Location location = new Location(LOCATION_CITY, LOCATION_ADDRESS);
        Group savedGroup = new Group(GROUP_NAME, owner);
        User user = new User(USERNAME, PASSWORD);

        userRepository.save(user);
        userRepository.save(owner);
        groupRepository.save(savedGroup);
        locationRepository.save(location);

        ownerDto = UserMapper.toDto(owner);
        locationDto = LocationMapper.toDto(location);
        groupDto = GroupMapper.toDto(savedGroup);
    }

    @Test
    void shouldCreateGroupEndpoint() throws Exception {
        String token = getAuthToken(mockMvc, USERNAME, PASSWORD);

        GroupDto request = createGroupRequest(GROUP_TITLE_1, ownerDto);

        MvcResult result = mockMvc.perform(post(CREATE_GROUP)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").value(GROUP_TITLE_1))
                .andExpect(jsonPath("$.owner.id").value(ownerDto.getId()))
                .andReturn();

        String content = result.getResponse().getContentAsString();

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        GroupDto group = objectMapper.readValue(content, GroupDto.class);

        mockMvc.perform(get(GET_GROUP, group.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(group.getName()))
                .andExpect(jsonPath("$.id").value(group.getId()));
    }

    @Test
    void shouldUpdateGroupEndpoint() throws Exception {
        String token = getAuthToken(mockMvc, OWNER, PASSWORD);

        GroupDto request = createGroupRequest(GROUP_TITLE_1, ownerDto);

        MvcResult result = mockMvc.perform(post(CREATE_GROUP)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").value(GROUP_TITLE_1))
                .andExpect(jsonPath("$.owner.id").value(ownerDto.getId()))
                .andReturn();

        String content = result.getResponse().getContentAsString();

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        GroupDto group = objectMapper.readValue(content, GroupDto.class);

        request.setName(GROUP_TITLE_2);
        mockMvc.perform(MockMvcRequestBuilders.put(UPDATE_GROUP, group.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(request.getName()));

        mockMvc.perform(get(GET_GROUP, group.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(GROUP_TITLE_2))
                .andExpect(jsonPath("$.id").value(group.getId()));
    }

    @Test
    void shouldRemoveGroupEndpoint() throws Exception {
        String token = getAuthToken(mockMvc, OWNER, PASSWORD);

        GroupDto request = createGroupRequest(GROUP_TITLE_1, ownerDto);

        MvcResult result = mockMvc.perform(post(CREATE_GROUP)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isCreated())
                .andReturn();

        String content = result.getResponse().getContentAsString();

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        GroupDto group = objectMapper.readValue(content, GroupDto.class);

        mockMvc.perform(delete(DELETE_GROUP, group.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isNoContent());

        mockMvc.perform(get(GET_GROUP, group.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldJoinGroupEndpoint() throws Exception {
        String token_owner = Utils.getAuthToken(mockMvc, OWNER, PASSWORD);
        String token_user = Utils.getAuthToken(mockMvc, USERNAME, PASSWORD);

        GroupDto request = createGroupRequest(GROUP_TITLE_1, ownerDto);

        MvcResult result = mockMvc.perform(post(CREATE_GROUP)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .header("Authorization", "Bearer " + token_owner))
                .andExpect(status().isCreated())
                .andReturn();

        String content = result.getResponse().getContentAsString();

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        GroupDto group = objectMapper.readValue(content, GroupDto.class);

        mockMvc.perform(get(JOIN_GROUP, group.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + token_user))
                .andExpect(status().isOk());

        mockMvc.perform(get(GROUP_MEMBERS, group.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + token_user))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));

        mockMvc.perform(get(LEAVE_GROUP, group.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + token_user))
                .andExpect(status().isOk());

        mockMvc.perform(get(GROUP_MEMBERS, group.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + token_user))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    void shouldOrganizeMeetingInTheGroupEndpoint() throws Exception, ResourceNotFoundException {
        String token_owner = getAuthToken(mockMvc, OWNER, PASSWORD);

        GroupDto request = createGroupRequest(GROUP_TITLE_1, ownerDto);

        MvcResult result = mockMvc.perform(post(CREATE_GROUP)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .header("Authorization", "Bearer " + token_owner))
                .andExpect(status().isCreated())
                .andReturn();

        String content = result.getResponse().getContentAsString();

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        GroupDto group = objectMapper.readValue(content, GroupDto.class);

        MeetupDto meetupRequest = createMeetupRequest(MEETUP_TITLE_1, MEETUP_DESCR, ownerDto, locationDto, group);
        mockMvc.perform(post(CREATE_MEETUP)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(meetupRequest))
                        .header("Authorization", "Bearer " + token_owner))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists());

        mockMvc.perform(get(MEETUPS_IN_THE_GROUP, group.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + token_owner))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

}
