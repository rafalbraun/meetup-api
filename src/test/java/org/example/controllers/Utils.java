package org.example.controllers;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.model.Group;
import org.example.model.Location;
import org.example.model.Meetup;
import org.example.model.User;
import org.example.repository.GroupRepository;
import org.example.repository.LocationRepository;
import org.example.repository.MeetupRepository;
import org.example.repository.UserRepository;
import org.example.service.MeetupService;
import org.junit.jupiter.api.AfterEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;
import static org.example.Constants.LOGIN_URL;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Component
public class Utils {

    public static final String MEETUP_TITLE = "Spring Boot Workshop";
    public static final String MEETUP_DESCR = "Spring Boot Workshop";
    public static final String LOCATION_ADDRESS = "Wilcza 10";
    public static final String LOCATION_CITY = "Warsaw";
    public static final String USERNAME = "test";
    public static final String PASSWORD = "test";
    public static final String GROUP_NAME = "Java Programmers";
    public static final String TEST_BIO = "test bio";

    public static String getAuthToken(MockMvc mockMvc, String username, String password) throws Exception {
        MvcResult loginResult = mockMvc.perform(post(LOGIN_URL)
                        .param("username", username)
                        .param("password", password))
                .andExpect(status().isOk())
                .andReturn();

        String token = loginResult.getResponse().getContentAsString();
        assertThat(token).isNotBlank();
        return token;
    }

    public static String toJson(Object obj, ObjectMapper objectMapper) throws Exception {
        return objectMapper.writeValueAsString(obj);
    }

    @Autowired
    public UserRepository userRepository;

    @Autowired
    public LocationRepository locationRepository;

    @Autowired
    public MeetupRepository meetupRepository;

    @Autowired
    public GroupRepository groupRepository;

    public User createUser(String username, String password) {
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        return userRepository.save(user);
    }

    public Location createLocation(String city, String address) {
        Location location = new Location();
        location.setCity(city);
        location.setAddress(address);
        return locationRepository.save(location);
    }

    public Group createGroup(String name, User user) {
        Group group = new Group();
        group.setName(name);
        group.setOwner(user);
        return groupRepository.save(group);
    }

    public Meetup createMeetup(String title, Instant time, User user, Group group, Location location) {
        Meetup meetup = new Meetup();
        meetup.setTitle(title);
        meetup.setDateTime(time);
        meetup.setOrganizer(user);
        meetup.setLocation(location);
        meetup.setGroup(group);
        return meetupRepository.save(meetup);
    }

    public void cleanDatabase() {
        meetupRepository.deleteAll();
        locationRepository.deleteAll();
        groupRepository.deleteAll();
        userRepository.deleteAll();
    }

}