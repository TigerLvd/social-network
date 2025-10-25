package com.highload.architect.soc.network.security;

import com.highload.architect.soc.network.exception.TokenExpiredException;
import com.highload.architect.soc.network.model.SimpleToken;
import com.highload.architect.soc.network.repository.SimpleTokenRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SimpleTokenProviderTest {

    @Mock
    private SimpleTokenRepository simpleTokenRepository;

    @Mock
    private HttpServletRequest request;

    @InjectMocks
    private SimpleTokenProvider simpleTokenProvider;

    private SimpleToken testToken;
    private UUID testTokenId;

    @BeforeEach
    void setUp() {
        testTokenId = UUID.randomUUID();
        testToken = new SimpleToken();
        testToken.setId(testTokenId);
        testToken.setUserId(UUID.randomUUID());
        testToken.setIssuedAt(LocalDateTime.now());
        testToken.setExpiration(LocalDateTime.now().plusDays(1));
    }

    @Test
    void shouldGetTokenFromRequest() {
        // Given
        when(request.getHeader("Authorization")).thenReturn("Bearer " + testTokenId.toString());
        when(simpleTokenRepository.findById(testTokenId)).thenReturn(Optional.of(testToken));

        // When
        SimpleToken result = simpleTokenProvider.getTokenFromRequest(request);

        // Then
        assertThat(result).isEqualTo(testToken);
        verify(simpleTokenRepository).findById(testTokenId);
    }

    @Test
    void shouldThrowExceptionWhenTokenNotFound() {
        // Given
        when(request.getHeader("Authorization")).thenReturn("Bearer " + testTokenId.toString());
        when(simpleTokenRepository.findById(testTokenId)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> simpleTokenProvider.getTokenFromRequest(request))
                .isInstanceOf(TokenExpiredException.class)
                .hasMessage("Token not found with ID: " + testTokenId);
    }

    @Test
    void shouldThrowExceptionForInvalidHeaderSize() {
        // Given
        when(request.getHeader("Authorization")).thenReturn("Bearer");

        // When & Then
        assertThatThrownBy(() -> simpleTokenProvider.getTokenFromRequest(request))
                .isInstanceOf(org.springframework.security.authentication.AuthenticationServiceException.class)
                .hasMessage("Invalid authorization header size.");
    }

    @Test
    void shouldThrowExceptionForNullHeader() {
        // Given
        when(request.getHeader("Authorization")).thenReturn(null);

        // When & Then
        assertThatThrownBy(() -> simpleTokenProvider.getTokenFromRequest(request))
                .isInstanceOf(org.springframework.security.authentication.AuthenticationServiceException.class)
                .hasMessage("Authorization header is missing");
    }

    @Test
    void shouldThrowExceptionForInvalidHeaderFormat() {
        // Given
        when(request.getHeader("Authorization")).thenReturn("InvalidFormat");

        // When & Then
        assertThatThrownBy(() -> simpleTokenProvider.getTokenFromRequest(request))
                .isInstanceOf(org.springframework.security.authentication.AuthenticationServiceException.class)
                .hasMessage("Invalid authorization header format");
    }

    @Test
    void shouldThrowExceptionForInvalidUuid() {
        // Given
        when(request.getHeader("Authorization")).thenReturn("Bearer invalid-uuid");

        // When & Then
        assertThatThrownBy(() -> simpleTokenProvider.getTokenFromRequest(request))
                .isInstanceOf(org.springframework.security.authentication.AuthenticationServiceException.class)
                .hasMessage("Invalid token format");
    }
}

