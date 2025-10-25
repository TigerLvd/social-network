package com.highload.architect.soc.network.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.highload.architect.soc.network.BaseIntegrationTest;
import com.highload.architect.soc.network.model.User;
import com.highload.architect.soc.network.model.UserInfo;
import com.highload.architect.soc.network.model.UserRegisterPostRequest;
import com.highload.architect.soc.network.repository.UserInfoRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureWebMvc
class UserApiIntegrationTest extends BaseIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserInfoRepository userInfoRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldRegisterUser() throws Exception {
        // Given
        UserRegisterPostRequest request = new UserRegisterPostRequest();
        request.setFirstName("John");
        request.setSecondName("Doe");
        request.setBirthdate(java.time.LocalDate.of(1994, 1, 1));
        request.setPassword("password123");

        // When & Then
        mockMvc.perform(post("/user/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").isNotEmpty());

        // Verify user was created in database
        assertThat(userInfoRepository.findAll()).hasSize(1);
    }

    @Test
    void shouldGetUserById() throws Exception {
        // Given
        UserInfo user = createTestUser();
        UserInfo savedUser = userInfoRepository.save(user);

        // When & Then
        mockMvc.perform(get("/user/get/{id}", savedUser.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(savedUser.getId().toString()))
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.secondName").value("Doe"));
    }

    @Test
    void shouldReturn404WhenUserNotFound() throws Exception {
        // Given
        UUID nonExistentId = UUID.randomUUID();

        // When & Then
        mockMvc.perform(get("/user/get/{id}", nonExistentId))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturn400ForInvalidUuid() throws Exception {
        // When & Then
        mockMvc.perform(get("/user/get/{id}", "invalid-uuid"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldSearchUsers() throws Exception {
        // Given
        UserInfo user1 = createTestUser();
        user1.setFirstName("John");
        user1.setSecondName("Doe");
        userInfoRepository.save(user1);

        UserInfo user2 = createTestUser();
        user2.setFirstName("John");
        user2.setSecondName("Smith");
        userInfoRepository.save(user2);

        // When & Then
        mockMvc.perform(get("/user/search")
                        .param("firstName", "John")
                        .param("lastName", "Doe"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].firstName").value("John"))
                .andExpect(jsonPath("$[0].secondName").value("Doe"));
    }

    @Test
    void shouldReturn400ForEmptySearchParameters() throws Exception {
        // When & Then
        mockMvc.perform(get("/user/search")
                        .param("firstName", "")
                        .param("lastName", ""))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturn400ForInvalidRegistrationData() throws Exception {
        // Given
        UserRegisterPostRequest request = new UserRegisterPostRequest();
        // Missing required fields

        // When & Then
        mockMvc.perform(post("/user/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    private UserInfo createTestUser() {
        UserInfo user = new UserInfo();
        user.setFirstName("John");
        user.setSecondName("Doe");
        user.setBirthdate(java.time.LocalDate.of(1994, 1, 1));
        user.setBiography("Test biography");
        user.setCity("Test City");
        return user;
    }
}
