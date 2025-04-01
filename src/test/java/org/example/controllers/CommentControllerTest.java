package org.example.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
public class CommentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldGetCommentEndpoint() throws Exception {}
    @Test
    void shouldGetMeetupCommentsEndpoint() throws Exception {}
    @Test
    void shouldCreateMeetupCommentEndpoint() throws Exception {}
    @Test
    void shouldUpdateCommentEndpoint() throws Exception {}
    @Test
    void shouldRemoveCommentEndpoint() throws Exception {}

}

