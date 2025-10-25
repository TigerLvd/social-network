package com.highload.architect.soc.network.service;

import com.highload.architect.soc.network.constants.SecurityConstants;
import com.highload.architect.soc.network.exception.TokenExpiredException;
import com.highload.architect.soc.network.model.SimpleToken;
import com.highload.architect.soc.network.repository.SimpleTokenRepository;
import com.highload.architect.soc.network.service.SimpleTokenService;
import com.highload.architect.soc.network.service.impl.SimpleTokenServiceImpl;
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
class SimpleTokenServiceImplTest {

    @Mock
    private SimpleTokenRepository simpleTokenRepository;

    @InjectMocks
    private SimpleTokenServiceImpl simpleTokenService;

    private SimpleToken testToken;
    private UUID testUserId;
    private UUID testTokenId;

    @BeforeEach
    void setUp() {
        testUserId = UUID.randomUUID();
        testTokenId = UUID.randomUUID();
        
        testToken = new SimpleToken();
        testToken.setId(testTokenId);
        testToken.setUserId(testUserId);
        testToken.setIssuedAt(LocalDateTime.now());
        testToken.setExpiration(LocalDateTime.now().plusDays(SecurityConstants.TOKEN_EXPIRATION_DAYS));
    }

    @Test
    void shouldGetTokenById() {
        // Given
        when(simpleTokenRepository.findById(testTokenId)).thenReturn(Optional.of(testToken));

        // When
        SimpleToken result = simpleTokenService.getSimpleTokenById(testTokenId);

        // Then
        assertThat(result).isEqualTo(testToken);
        assertThat(result.getId()).isEqualTo(testTokenId);
        assertThat(result.getUserId()).isEqualTo(testUserId);
        verify(simpleTokenRepository).findById(testTokenId);
    }

    @Test
    void shouldThrowExceptionWhenTokenNotFound() {
        // Given
        when(simpleTokenRepository.findById(testTokenId)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> simpleTokenService.getSimpleTokenById(testTokenId))
                .isInstanceOf(TokenExpiredException.class)
                .hasMessage("Token not found with ID: " + testTokenId);
    }

    @Test
    void shouldCreateToken() {
        // Given
        when(simpleTokenRepository.save(any(SimpleToken.class))).thenReturn(testToken);

        // When
        SimpleToken result = simpleTokenService.createSimpleToken(testUserId);

        // Then
        assertThat(result).isEqualTo(testToken);
        verify(simpleTokenRepository).save(any(SimpleToken.class));
    }

    @Test
    void shouldSetCorrectExpirationWhenCreatingToken() {
        // Given
        when(simpleTokenRepository.save(any(SimpleToken.class))).thenAnswer(invocation -> {
            SimpleToken token = invocation.getArgument(0);
            assertThat(token.getUserId()).isEqualTo(testUserId);
            assertThat(token.getIssuedAt()).isNotNull();
            assertThat(token.getExpiration()).isAfter(token.getIssuedAt());
            return token;
        });

        // When
        simpleTokenService.createSimpleToken(testUserId);

        // Then
        verify(simpleTokenRepository).save(any(SimpleToken.class));
    }
}
