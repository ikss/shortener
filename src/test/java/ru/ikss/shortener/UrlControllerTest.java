package ru.ikss.shortener;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;

import ru.ikss.shortener.model.UrlRequest;
import ru.ikss.shortener.model.UrlResponse;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("URL controller tests")
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("default, development, test")
@ExtendWith(SpringExtension.class)
public class UrlControllerTest {
    private static final String TEST_ACCOUNT = "test_account";
    private static final String TEST_PASSWORD = "test_password";
    private static final String DEFAULT_HTTPS_URL = "https://google.com";
    private static final String DEFAULT_HTTP_URL = "http://google.com";
    private static final String DEFAULT_URL_WITHOUT_SCHEMA = "google.com";
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Value("${server.redirectUri}")
    private String redirectUri;

    @Test
    @DisplayName("Register url with unknown account")
    public void registerUrlWithUnknownAccountTest() throws Exception {
        mockMvc.perform(
            MockMvcRequestBuilders
                .post("/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"url\":\"" + DEFAULT_HTTPS_URL + "\"}")
        ).andExpect(status().isUnauthorized());
    }

    @Test
    @Transactional
    @WithMockUser(username = TEST_ACCOUNT, password = TEST_PASSWORD)
    @DisplayName("Register url with test account and default status")
    public void registerUrlTest() throws Exception {
        MockHttpServletResponse response = mockMvc.perform(
            MockMvcRequestBuilders
                .post("/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"url\":\"" + DEFAULT_HTTPS_URL + "\"}")
        ).andExpect(status().isCreated())
            .andReturn().getResponse();
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
        mockMvc.perform(
            MockMvcRequestBuilders
                .post("/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"url\":\"" + DEFAULT_HTTPS_URL + "\"}")
        ).andExpect(status().isCreated());
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
        MockHttpServletResponse response = mockMvc.perform(
            MockMvcRequestBuilders
                .post("/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"url\":\"" + DEFAULT_URL_WITHOUT_SCHEMA + "\"}")
        ).andExpect(status().isCreated())
            .andReturn().getResponse();
        UrlResponse url = objectMapper.readValue(response.getContentAsString(), UrlResponse.class);
        String shortUrl = url.getShortUrl();
        assertNotNull(shortUrl, "Short url not null");
        mockMvc.perform(MockMvcRequestBuilders.get(shortUrl))
            .andExpect(status().isFound())
            .andExpect(header().string(HttpHeaders.LOCATION, DEFAULT_HTTP_URL));
    }
}
