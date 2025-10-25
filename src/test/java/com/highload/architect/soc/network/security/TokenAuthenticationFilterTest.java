package com.highload.architect.soc.network.security;

import com.highload.architect.soc.network.model.AccountInfo;
import com.highload.architect.soc.network.model.SimpleToken;
import com.highload.architect.soc.network.model.UserInfo;
import com.highload.architect.soc.network.service.AccountInfoService;
import com.highload.architect.soc.network.service.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.Authentication;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TokenAuthenticationFilterTest {

    @Mock
    private AccountInfoService accountInfoService;

    @Mock
    private SimpleTokenProvider tokenProvider;

    @Mock
    private UserService userService;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain filterChain;

    @InjectMocks
    private TokenAuthenticationFilter tokenAuthenticationFilter;

    private UserInfo testUser;
    private AccountInfo testAccount;
    private SimpleToken testToken;

    @BeforeEach
    void setUp() {
        testUser = new UserInfo();
        testUser.setId(UUID.randomUUID());
        testUser.setFirstName("John");
        testUser.setSecondName("Doe");

        testAccount = new AccountInfo(testUser.getId(), "encodedPassword");

        testToken = new SimpleToken();
        testToken.setId(UUID.randomUUID());
        testToken.setUserId(testUser.getId());
        testToken.setIssuedAt(LocalDateTime.now());
        testToken.setExpiration(LocalDateTime.now().plusDays(1));

        SecurityContextHolder.clearContext();
    }

    @Test
    void shouldAuthenticateWithValidToken() throws Exception {
        // Given
        when(request.getHeader("Authorization")).thenReturn("Bearer " + testToken.getId().toString());
        when(tokenProvider.getTokenFromRequest(request)).thenReturn(testToken);
        when(userService.getById(testUser.getId())).thenReturn(testUser);
        when(accountInfoService.getById(testUser.getId())).thenReturn(testAccount);

        // When
        tokenAuthenticationFilter.doFilterInternal(request, response, filterChain);

        // Then
        verify(userService).getById(testUser.getId());
        verify(accountInfoService).getById(testUser.getId());
        verify(filterChain).doFilter(request, response);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        assertThat(auth).isNotNull();
    }

    @Test
    void shouldNotAuthenticateWithoutAuthorizationHeader() throws Exception {
        // Given
        when(request.getHeader("Authorization")).thenReturn(null);

        // When
        tokenAuthenticationFilter.doFilterInternal(request, response, filterChain);

        // Then
        verify(tokenProvider, never()).getTokenFromRequest(any());
        verify(filterChain).doFilter(request, response);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        assertThat(auth).isNull();
    }

    @Test
    void shouldNotAuthenticateWithInvalidAuthorizationHeader() throws Exception {
        // Given
        when(request.getHeader("Authorization")).thenReturn("InvalidHeader");

        // When
        tokenAuthenticationFilter.doFilterInternal(request, response, filterChain);

        // Then
        verify(tokenProvider, never()).getTokenFromRequest(any());
        verify(filterChain).doFilter(request, response);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        assertThat(auth).isNull();
    }

    @Test
    void shouldNotAuthenticateWhenUserNotFound() throws Exception {
        // Given
        when(request.getHeader("Authorization")).thenReturn("Bearer " + testToken.getId().toString());
        when(tokenProvider.getTokenFromRequest(request)).thenReturn(testToken);
        when(userService.getById(testUser.getId())).thenReturn(null);

        // When
        tokenAuthenticationFilter.doFilterInternal(request, response, filterChain);

        // Then
        verify(userService).getById(testUser.getId());
        verify(filterChain).doFilter(request, response);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        assertThat(auth).isNull();
    }

    @Test
    void shouldNotAuthenticateWhenAccountNotFound() throws Exception {
        // Given
        when(request.getHeader("Authorization")).thenReturn("Bearer " + testToken.getId().toString());
        when(tokenProvider.getTokenFromRequest(request)).thenReturn(testToken);
        when(userService.getById(testUser.getId())).thenReturn(testUser);
        when(accountInfoService.getById(testUser.getId())).thenReturn(null);

        // When
        tokenAuthenticationFilter.doFilterInternal(request, response, filterChain);

        // Then
        verify(userService).getById(testUser.getId());
        verify(accountInfoService).getById(testUser.getId());
        verify(filterChain).doFilter(request, response);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        assertThat(auth).isNull();
    }

    @Test
    void shouldHandleExceptionGracefully() throws Exception {
        // Given
        when(request.getHeader("Authorization")).thenReturn("Bearer " + testToken.getId().toString());
        when(tokenProvider.getTokenFromRequest(request)).thenThrow(new RuntimeException("Token error"));

        // When
        tokenAuthenticationFilter.doFilterInternal(request, response, filterChain);

        // Then
        verify(filterChain).doFilter(request, response);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        assertThat(auth).isNull();
    }
}
