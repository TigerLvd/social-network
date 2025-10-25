package com.highload.architect.soc.network.service;

import com.highload.architect.soc.network.exception.UserNotFoundException;
import com.highload.architect.soc.network.model.AccountInfo;
import com.highload.architect.soc.network.repository.AccountInfoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AccountInfoServiceImplTest {

    @Mock
    private AccountInfoRepository accountInfoRepository;

    @InjectMocks
    private AccountInfoServiceImpl accountInfoService;

    private AccountInfo testAccountInfo;
    private UUID testUserId;

    @BeforeEach
    void setUp() {
        testUserId = UUID.randomUUID();
        testAccountInfo = new AccountInfo(testUserId, "encodedPassword");
    }

    @Test
    void shouldGetAccountById() {
        // Given
        when(accountInfoRepository.findById(testUserId)).thenReturn(Optional.of(testAccountInfo));

        // When
        AccountInfo result = accountInfoService.getById(testUserId);

        // Then
        assertThat(result).isEqualTo(testAccountInfo);
        assertThat(result.getId()).isEqualTo(testUserId);
        assertThat(result.getPassword()).isEqualTo("encodedPassword");
        verify(accountInfoRepository).findById(testUserId);
    }

    @Test
    void shouldThrowExceptionWhenAccountNotFound() {
        // Given
        when(accountInfoRepository.findById(testUserId)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> accountInfoService.getById(testUserId))
                .isInstanceOf(UserNotFoundException.class)
                .hasMessage("Account not found with ID: " + testUserId);
    }

    @Test
    void shouldSaveAccount() {
        // Given
        when(accountInfoRepository.save(any(AccountInfo.class))).thenReturn(testAccountInfo);

        // When
        accountInfoService.save(testAccountInfo);

        // Then
        verify(accountInfoRepository).save(testAccountInfo);
    }
}
