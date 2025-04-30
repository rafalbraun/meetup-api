package org.example.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.dto.GroupDto;
import org.example.dto.LocationDto;
import org.example.dto.MeetupDto;
import org.example.dto.UserDto;
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

    public static final String MEETUP_TITLE_1 = "Spring Boot Workshop 1";
    public static final String MEETUP_TITLE_2 = "Spring Boot Workshop 2";
    public static final String MEETUP_TITLE_3 = "Spring Boot Workshop 3";
    public static final String MEETUP_DESCR = "Spring Boot Workshop";
    public static final String[] MEETUP_TITLES = new String[]{MEETUP_TITLE_1, MEETUP_TITLE_2, MEETUP_TITLE_3};
    public static final String LOCATION_ADDRESS = "Wilcza 10";
    public static final String LOCATION_CITY = "Warsaw";
    public static final String USERNAME = "user";
    public static final String PASSWORD = "test";
    public static final String OWNER = "owner";
    public static final String GROUP_NAME = "Java Programmers";
    public static final String TEST_BIO = "test bio";
    public static final String GROUP_TITLE_1 = "group 1";
    public static final String GROUP_TITLE_2 = "group 2";
    public static final String GROUP_TITLE_3 = "group 3";

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

    public static MeetupDto createMeetupRequest(String title, String description, UserDto userDto, LocationDto locationDto, GroupDto groupDto) {
        MeetupDto request = new MeetupDto();
        request.setTitle(title);
        request.setDescription(description);
        request.setDateTime(Instant.now());
        request.setOrganizer(userDto);
        request.setLocation(locationDto);
        request.setGroup(groupDto);
        return request;
    }

    public static GroupDto createGroupRequest(String name, UserDto owner) {
        GroupDto request = new GroupDto();
        request.setName(name);
        request.setOwner(owner);
        return request;
    }

}