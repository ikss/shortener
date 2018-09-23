package ru.ikss.shortener.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import ru.ikss.shortener.model.UrlRequest;
import ru.ikss.shortener.model.UrlResponse;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("URL controller tests")
public class UrlControllerTest extends AbstractControllerTest {

    @Test
    @DisplayName("Register url with unknown account")
    public void registerUrlWithUnknownAccountTest() throws Exception {
        registerUrlAndCheck(DEFAULT_HTTPS_URL, status().isUnauthorized());
    }

    @Test
    @Transactional
    @WithMockUser(username = TEST_ACCOUNT, password = TEST_PASSWORD)
    @DisplayName("Register url with test account and default status")
    public void registerUrlTest() throws Exception {
        MockHttpServletResponse response = registerUrl(DEFAULT_HTTPS_URL);
        UrlResponse url = objectMapper.readValue(response.getContentAsString(), UrlResponse.class);
        String shortUrl = url.getShortUrl();
        assertNotNull(shortUrl, "Short url not null");
        mockMvc.perform(MockMvcRequestBuilders.get(shortUrl))
            .andExpect(status().isFound())
            .andExpect(header().string(HttpHeaders.LOCATION, DEFAULT_HTTPS_URL));
    }

    @Test
    @Transactional
    @WithMockUser(username = TEST_ACCOUNT, password = TEST_PASSWORD)
    @DisplayName("Register url with test account and moved permanently status")
    public void registerUrlTestWithMovedPermanentlyStatus() throws Exception {
        UrlRequest request = new UrlRequest().setUrl(DEFAULT_HTTPS_URL).setRedirectType(HttpStatus.MOVED_PERMANENTLY.value());
        MockHttpServletResponse response = mockMvc.perform(
            MockMvcRequestBuilders
                .post("/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        ).andExpect(status().isCreated())
            .andReturn().getResponse();
        UrlResponse url = objectMapper.readValue(response.getContentAsString(), UrlResponse.class);
        String shortUrl = url.getShortUrl();
        assertNotNull(shortUrl, "Short url not null");
        mockMvc.perform(MockMvcRequestBuilders.get(shortUrl))
            .andExpect(status().isMovedPermanently())
            .andExpect(header().string(HttpHeaders.LOCATION, DEFAULT_HTTPS_URL));
    }

    @Test
    @Transactional
    @WithMockUser(username = TEST_ACCOUNT, password = TEST_PASSWORD)
    @DisplayName("Register duplicated url with same status")
    public void registerDuplicatedUrlWithSameStatus() throws Exception {
        registerUrl(DEFAULT_HTTPS_URL);
        mockMvc.perform(
            MockMvcRequestBuilders
                .post("/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"url\":\"" + DEFAULT_HTTPS_URL + "\"}")
        ).andExpect(status().isCreated());
    }

    @Test
    @Transactional
    @WithMockUser(username = TEST_ACCOUNT, password = TEST_PASSWORD)
    @DisplayName("Register url without schema")
    public void registerUrlWithoutSchemaTest() throws Exception {
        MockHttpServletResponse response = registerUrl(DEFAULT_URL_WITHOUT_SCHEMA);
        UrlResponse url = objectMapper.readValue(response.getContentAsString(), UrlResponse.class);
        assertNotNull(url.getShortUrl(), "Short url not null");
        mockMvc.perform(MockMvcRequestBuilders.get(url.getShortUrl()))
            .andExpect(status().isFound())
            .andExpect(header().string(HttpHeaders.LOCATION, DEFAULT_HTTP_URL));
    }

    @Test
    @Transactional
    @WithMockUser(username = TEST_ACCOUNT, password = TEST_PASSWORD)
    @DisplayName("Register illegal url")
    public void registerInvalidUrlTest() throws Exception {
        registerUrlAndCheck("www..com", status().isBadRequest());
    }
}
