package com.highload.architect.soc.network.api;

import com.highload.architect.soc.network.model.AccountInfo;
import com.highload.architect.soc.network.model.SimpleToken;
import com.highload.architect.soc.network.service.AccountInfoService;
import com.highload.architect.soc.network.service.SimpleTokenService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.context.annotation.Import;
import com.highload.architect.soc.network.config.PasswordEncoderConfig;

import java.util.UUID;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(LoginApiImpl.class)
@Import(PasswordEncoderConfig.class)
@AutoConfigureMockMvc(addFilters = false)
class LoginApiImplTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AccountInfoService accountInfoService;

    @MockitoBean
    private SimpleTokenService simpleTokenService;

    @MockitoBean
    private PasswordEncoder passwordEncoder;

    private final UUID testUserId = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");
    private final String testPassword = "password123";

    @Test
    void loginPost_SuccessfulAuthentication_Returns200WithToken() throws Exception {
        // Arrange
        AccountInfo accountInfo = new AccountInfo(testUserId, "$2a$10$encodedPassword");
        SimpleToken simpleToken = new SimpleToken();
        simpleToken.setId(UUID.randomUUID());

        when(accountInfoService.getById(testUserId)).thenReturn(accountInfo);
        when(passwordEncoder.matches(testPassword, "$2a$10$encodedPassword")).thenReturn(true);
        when(simpleTokenService.createSimpleToken(testUserId)).thenReturn(simpleToken);

        // Act & Assert
        mockMvc.perform(post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"id\": \"" + testUserId + "\", \"password\": \"" + testPassword + "\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists());
    }

    @Test
    void loginPost_InvalidCredentials_Returns404() throws Exception {
        // Arrange
        AccountInfo accountInfo = new AccountInfo(testUserId, "$2a$10$differentEncodedPassword");

        when(accountInfoService.getById(testUserId)).thenReturn(accountInfo);
        when(passwordEncoder.matches(testPassword, "$2a$10$differentEncodedPassword")).thenReturn(false);

        // Act & Assert
        mockMvc.perform(post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"id\": \"" + testUserId + "\", \"password\": \"" + testPassword + "\"}"))
                .andExpect(status().isNotFound());
    }

    @Test
    void loginPost_InvalidUuidFormat_Returns400() throws Exception {
        // Act & Assert
        mockMvc.perform(post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"id\": \"invalid-uuid\", \"password\": \"" + testPassword + "\"}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void loginPost_ServiceError_Returns503() throws Exception {
        // Arrange
        when(accountInfoService.getById(testUserId)).thenThrow(new RuntimeException("Service unavailable"));

        // Act & Assert
        mockMvc.perform(post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"id\": \"" + testUserId + "\", \"password\": \"" + testPassword + "\"}"))
                .andExpect(status().isServiceUnavailable());
    }
}
