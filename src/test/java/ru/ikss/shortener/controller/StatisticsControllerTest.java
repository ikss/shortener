package ru.ikss.shortener.controller;

import java.util.Map;

import org.apache.commons.lang3.RandomUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import ru.ikss.shortener.model.UrlResponse;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("Statistics controller tests")
public class StatisticsControllerTest extends AbstractControllerTest {

    @Test
    @DisplayName("Statistic disabled for unautorized account")
    public void statisticForUnauthorizedTest() throws Exception {
        getStatisticAndCheck("/statistic/" + TEST_ACCOUNT, status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = TEST_ACCOUNT, password = TEST_PASSWORD)
    @DisplayName("Statistic returns only with accountId")
    public void statisticOnlyWithAccountIdTest() throws Exception {
        getStatisticAndCheck("/statistic/", status().isNotFound());
    }

    @Test
    @Transactional
    @WithMockUser(username = TEST_ACCOUNT, password = TEST_PASSWORD)
    @DisplayName("Register url and get zero in statistic")
    public void checkNewUrlStatistic() throws Exception {
        registerUrl(DEFAULT_HTTP_URL);
        MockHttpServletResponse response = getStatisticAndCheck("/statistic/" + TEST_ACCOUNT, status().isOk());
        Map<String, Integer> statistic = (Map<String, Integer>) objectMapper.readValue(response.getContentAsString(), Map.class);
        assertEquals(1, statistic.entrySet().size(), "One url in statistic");
        Integer count = statistic.get(DEFAULT_HTTP_URL);
        assertNotNull(count, "Only " + DEFAULT_HTTP_URL + " url is in statistic");
        assertEquals(Integer.valueOf(0), count, "Zero redirects in statistic");
    }

    @Test
    @Transactional
    @WithMockUser(username = TEST_ACCOUNT, password = TEST_PASSWORD)
    @DisplayName("Check count of redirects illegal url")
    public void checkUrlAfterSomeRedirects() throws Exception {
        MockHttpServletResponse registerResponse = registerUrl(DEFAULT_HTTP_URL);
        UrlResponse url = objectMapper.readValue(registerResponse.getContentAsString(), UrlResponse.class);
        int count = RandomUtils.nextInt(1, 6);
        getUrl(url.getShortUrl(), count);
        MockHttpServletResponse response = getStatisticAndCheck("/statistic/" + TEST_ACCOUNT, status().isOk());
        Map<String, Integer> statistic = (Map<String, Integer>) objectMapper.readValue(response.getContentAsString(), Map.class);
        assertEquals(1, statistic.entrySet().size(), "One url in statistic");
        assertEquals(Integer.valueOf(count), statistic.get(DEFAULT_HTTP_URL), "Zero redirects in statistic");
    }

    private void getUrl(String url, int count) throws Exception {
        for (int i = 0; i < count; i++) {
            mockMvc.perform(MockMvcRequestBuilders.get(url));
        }
    }
}
