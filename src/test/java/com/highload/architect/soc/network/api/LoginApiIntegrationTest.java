package com.highload.architect.soc.network.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.highload.architect.soc.network.BaseIntegrationTest;
import com.highload.architect.soc.network.model.AccountInfo;
import com.highload.architect.soc.network.model.LoginPostRequest;
import com.highload.architect.soc.network.model.UserInfo;
import com.highload.architect.soc.network.repository.AccountInfoRepository;
import com.highload.architect.soc.network.repository.UserInfoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureWebMvc
class LoginApiIntegrationTest extends BaseIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserInfoRepository userInfoRepository;

    @Autowired
    private AccountInfoRepository accountInfoRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ObjectMapper objectMapper;

    private UserInfo testUser;
    private AccountInfo testAccount;

    @BeforeEach
    public void setUp() {
        // Create test user
        testUser = new UserInfo();
        testUser.setFirstName("John");
        testUser.setSecondName("Doe");
        testUser.setBirthdate(java.time.LocalDate.of(1994, 1, 1));
        UserInfo savedUser = userInfoRepository.save(testUser);

        // Create test account with encoded password
        String encodedPassword = passwordEncoder.encode("password123");
        testAccount = new AccountInfo(savedUser.getId(), encodedPassword);
        accountInfoRepository.save(testAccount);
    }

    @Test
    void shouldLoginWithValidCredentials() throws Exception {
        // Given
        LoginPostRequest request = new LoginPostRequest();
        request.setId(testUser.getId().toString());
        request.setPassword("password123");

        // When & Then
        mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").isNotEmpty());
    }

    @Test
    void shouldReturn404ForInvalidCredentials() throws Exception {
        // Given
        LoginPostRequest request = new LoginPostRequest();
        request.setId(testUser.getId().toString());
        request.setPassword("wrongpassword");

        // When & Then
        mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturn404ForNonExistentUser() throws Exception {
        // Given
        LoginPostRequest request = new LoginPostRequest();
        request.setId(UUID.randomUUID().toString());
        request.setPassword("password123");

        // When & Then
        mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturn400ForInvalidUuid() throws Exception {
        // Given
        LoginPostRequest request = new LoginPostRequest();
        request.setId("invalid-uuid");
        request.setPassword("password123");

        // When & Then
        mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturn400ForInvalidRequestData() throws Exception {
        // Given
        LoginPostRequest request = new LoginPostRequest();
        // Missing required fields

        // When & Then
        mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }
}
