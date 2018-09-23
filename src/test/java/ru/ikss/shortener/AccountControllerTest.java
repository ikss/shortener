package ru.ikss.shortener;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;

import ru.ikss.shortener.model.AccountResponse;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("Account controller tests")
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("default, development, test")
@ExtendWith(SpringExtension.class)
public class AccountControllerTest {
    private static final String TEST_ACCOUNT = "test_account";
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @Transactional
    @DisplayName("Create account with empty body")
    public void createAccountWithEmptyBodyTest() throws Exception {
        mockMvc.perform(
            MockMvcRequestBuilders
                .post("/account")
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isBadRequest());
    }

    @Test
    @Transactional
    @DisplayName("Create account with empty id")
    public void createAccountWithEmptyIdTest() throws Exception {
        mockMvc.perform(
            MockMvcRequestBuilders
                .post("/account")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"AccountId\":\"\"}")
        ).andExpect(status().isBadRequest());
    }

    @Test
    @Transactional
    @DisplayName("Create account successfully")
    public void successCreateAccountTest() throws Exception {
        String accountId = RandomStringUtils.randomAlphanumeric(10);
        MockHttpServletResponse response = mockMvc.perform(
            MockMvcRequestBuilders
                .post("/account")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"AccountId\":\"" + accountId + "\"}")
        ).andExpect(status().isCreated())
            .andReturn().getResponse();
        AccountResponse account = objectMapper.readValue(response.getContentAsString(), AccountResponse.class);
        assertTrue(account.isSuccess(), "Account created successfully");
        assertNotNull(account.getPassword(), "Password returned");
    }

    @Test
    @Transactional
    @DisplayName("Duplicate account creation")
    public void duplicateCreateAccountTest() throws Exception {
        String accountId = RandomStringUtils.randomAlphanumeric(10);
        mockMvc.perform(
            MockMvcRequestBuilders
                .post("/account")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"AccountId\":\"" + accountId + "\"}")
        ).andExpect(status().isCreated());
        MockHttpServletResponse response = mockMvc.perform(
            MockMvcRequestBuilders
                .post("/account")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"AccountId\":\"" + accountId + "\"}")
        ).andExpect(status().isConflict())
            .andReturn().getResponse();
        AccountResponse account = objectMapper.readValue(response.getContentAsString(), AccountResponse.class);
        assertFalse(account.isSuccess(), "Account didn't create");
        assertNull(account.getPassword(), "Password is null");
    }

    @Test
    @Transactional
    @DisplayName("Checks test account exists")
    public void testAccountExistsTest() throws Exception {
        mockMvc.perform(
            MockMvcRequestBuilders
                .post("/account")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"AccountId\":\"" + TEST_ACCOUNT + "\"}")
        ).andExpect(status().isConflict());
    }
}
