package ru.ikss.shortener.controller;

import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("default, override, test")
@ExtendWith(SpringExtension.class)
public abstract class AbstractControllerTest {
    static final String TEST_ACCOUNT = "test_account";
    static final String TEST_PASSWORD = "test_password";
    static final String DEFAULT_HTTPS_URL = "https://google.com";
    static final String DEFAULT_HTTP_URL = "http://google.com";
    static final String DEFAULT_URL_WITHOUT_SCHEMA = "google.com";
    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;

    MockHttpServletResponse registerUrl(String url) throws Exception {
        return registerUrlAndCheck(url, status().isCreated());
    }

    MockHttpServletResponse registerUrlAndCheck(String url, ResultMatcher matcher) throws Exception {
        return mockMvc.perform(
            MockMvcRequestBuilders
                .post("/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"url\":\"" + url + "\"}")
        ).andExpect(matcher).andReturn().getResponse();
    }

    MockHttpServletResponse getStatisticAndCheck(String url, ResultMatcher matcher) throws Exception {
        return mockMvc.perform(
            MockMvcRequestBuilders
                .get(url)
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(matcher).andReturn().getResponse();
    }
}
