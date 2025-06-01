package org.example.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
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
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.example.Constants.*;
import static org.example.controllers.Utils.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class AuthIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    public UserRepository userRepository;

    @Test
    void shouldAuthenticateAndAccessSecuredEndpoint() throws Exception {

        // preconditions
        User user = new User(USERNAME, PASSWORD);
        userRepository.save(user);

        // test
        MvcResult loginResult = mockMvc.perform(post(LOGIN_URL)
                        .param("username", USERNAME)
                        .param("password", PASSWORD))
                .andExpect(status().isOk())
                .andReturn();

        String token = loginResult.getResponse().getContentAsString();
        assertThat(token).isNotBlank();

        MvcResult helloResult = mockMvc.perform(get(TEST)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andReturn();

        String response = helloResult.getResponse().getContentAsString();
        assertThat(response).isEqualTo(TEST_STRING);

    }

}
