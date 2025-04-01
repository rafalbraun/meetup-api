package org.example.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.http.MediaType;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;

public class MockMvcTestHelper {

    @Autowired
    private MockMvc mockMvc;

    public MockMvcTestHelper(MockMvc mockMvc) {
        this.mockMvc = mockMvc;
    }

    public ResultActions performAuthGet(String token, String urlTemplate, Object... uriVars) throws Exception {
        return mockMvc.perform(get(urlTemplate, uriVars)
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + token));
    }

    public ResultActions performAuthPut(String token, String urlTemplate, Object... uriVars) throws Exception {
        return mockMvc.perform(put(urlTemplate, uriVars)
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + token));
    }

    public ResultActions performAuthPost(String token, String urlTemplate, Object... uriVars) throws Exception {
        return mockMvc.perform(post(urlTemplate, uriVars)
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + token));
    }

    public ResultActions performAuthDelete(String token, String urlTemplate, Object... uriVars) throws Exception {
        return mockMvc.perform(delete(urlTemplate, uriVars)
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + token));
    }

}
