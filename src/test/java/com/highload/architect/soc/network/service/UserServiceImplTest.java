package com.highload.architect.soc.network.service;

import com.highload.architect.soc.network.exception.UserNotFoundException;
import com.highload.architect.soc.network.mapper.UserInfoMapper;
import com.highload.architect.soc.network.model.AccountInfo;
import com.highload.architect.soc.network.model.User;
import com.highload.architect.soc.network.model.UserInfo;
import com.highload.architect.soc.network.model.UserRegisterPostRequest;
import com.highload.architect.soc.network.repository.AccountInfoRepository;
import com.highload.architect.soc.network.repository.UserInfoRepository;
import com.highload.architect.soc.network.service.UserService;
import com.highload.architect.soc.network.service.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private AccountInfoRepository accountInfoRepository;

    @Mock
    private UserInfoRepository userInfoRepository;

    @Mock
    private UserInfoMapper userInfoMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    private UserInfo testUserInfo;
    private User testUser;
    private UserRegisterPostRequest testRequest;
    private AccountInfo testAccountInfo;

    @BeforeEach
    void setUp() {
        testUserInfo = new UserInfo();
        testUserInfo.setId(UUID.randomUUID());
        testUserInfo.setFirstName("John");
        testUserInfo.setSecondName("Doe");
        testUserInfo.setBirthdate(java.time.LocalDate.of(1994, 1, 1));

        testUser = new User();
        testUser.setId(testUserInfo.getId());
        testUser.setFirstName("John");
        testUser.setSecondName("Doe");
        testUser.setBirthdate(java.time.LocalDate.of(1994, 1, 1));

        testRequest = new UserRegisterPostRequest();
        testRequest.setFirstName("John");
        testRequest.setSecondName("Doe");
        testRequest.setBirthdate(java.time.LocalDate.of(1994, 1, 1));
        testRequest.setPassword("password123");

        testAccountInfo = new AccountInfo(testUserInfo.getId(), "encodedPassword");
    }

    @Test
    void shouldGetUserById() {
        // Given
        UUID userId = UUID.randomUUID();
        when(userInfoRepository.findById(userId)).thenReturn(Optional.of(testUserInfo));
        when(userInfoMapper.toDto(testUserInfo)).thenReturn(testUser);

        // When
        User result = userService.getUser(userId);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getFirstName()).isEqualTo("John");
        assertThat(result.getSecondName()).isEqualTo("Doe");
        verify(userInfoRepository).findById(userId);
        verify(userInfoMapper).toDto(testUserInfo);
    }

    @Test
    void shouldThrowExceptionWhenUserNotFound() {
        // Given
        UUID userId = UUID.randomUUID();
        when(userInfoRepository.findById(userId)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> userService.getUser(userId))
                .isInstanceOf(UserNotFoundException.class)
                .hasMessage("User not found with ID: " + userId);
    }

    @Test
    void shouldCreateUser() {
        // Given
        when(userInfoMapper.toEntity(testRequest)).thenReturn(testUserInfo);
        when(userInfoRepository.save(testUserInfo)).thenReturn(testUserInfo);
        when(passwordEncoder.encode("password123")).thenReturn("encodedPassword");
        when(accountInfoRepository.save(any(AccountInfo.class))).thenReturn(testAccountInfo);

        // When
        UUID result = userService.create(testRequest);

        // Then
        assertThat(result).isEqualTo(testUserInfo.getId());
        verify(userInfoMapper).toEntity(testRequest);
        verify(userInfoRepository).save(testUserInfo);
        verify(passwordEncoder).encode("password123");
        verify(accountInfoRepository).save(any(AccountInfo.class));
    }

    @Test
    void shouldFindUsersByFirstNameAndSecondName() {
        // Given
        List<UserInfo> userInfos = List.of(testUserInfo);
        when(userInfoRepository.findByFirstNameAndSecondName("John", "Doe")).thenReturn(userInfos);
        when(userInfoMapper.toDto(testUserInfo)).thenReturn(testUser);

        // When
        List<User> result = userService.findByFirstNameAndSecondName("John", "Doe");

        // Then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getFirstName()).isEqualTo("John");
        assertThat(result.get(0).getSecondName()).isEqualTo("Doe");
        verify(userInfoRepository).findByFirstNameAndSecondName("John", "Doe");
        verify(userInfoMapper).toDto(testUserInfo);
    }

    @Test
    void shouldReturnEmptyListWhenNoUsersFound() {
        // Given
        when(userInfoRepository.findByFirstNameAndSecondName("NonExistent", "User")).thenReturn(null);

        // When
        List<User> result = userService.findByFirstNameAndSecondName("NonExistent", "User");

        // Then
        assertThat(result).isEmpty();
        verify(userInfoRepository).findByFirstNameAndSecondName("NonExistent", "User");
    }

    @Test
    void shouldGetUserInfoById() {
        // Given
        UUID userId = UUID.randomUUID();
        when(userInfoRepository.findById(userId)).thenReturn(Optional.of(testUserInfo));

        // When
        UserInfo result = userService.getById(userId);

        // Then
        assertThat(result).isEqualTo(testUserInfo);
        verify(userInfoRepository).findById(userId);
    }

    @Test
    void shouldThrowExceptionWhenUserInfoNotFound() {
        // Given
        UUID userId = UUID.randomUUID();
        when(userInfoRepository.findById(userId)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> userService.getById(userId))
                .isInstanceOf(UserNotFoundException.class)
                .hasMessage("User not found with ID: " + userId);
    }
}
