package com.highload.architect.soc.network.repository;

import com.highload.architect.soc.network.BaseIntegrationTest;
import com.highload.architect.soc.network.model.AccountInfo;
import com.highload.architect.soc.network.model.UserInfo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class AccountInfoRepositoryTest extends BaseIntegrationTest {

    @Autowired
    private AccountInfoRepository accountInfoRepository;

    @Autowired
    private UserInfoRepository userInfoRepository;

    @Test
    void shouldFindAccountById() {
        // Given
        UserInfo user = createTestUser();
        UserInfo savedUser = userInfoRepository.save(user);
        
        AccountInfo account = new AccountInfo(savedUser.getId(), "encodedPassword");
        AccountInfo savedAccount = accountInfoRepository.save(account);

        // When
        Optional<AccountInfo> foundAccount = accountInfoRepository.findById(savedUser.getId());

        // Then
        assertThat(foundAccount).isPresent();
        assertThat(foundAccount.get().getId()).isEqualTo(savedUser.getId());
        assertThat(foundAccount.get().getPassword()).isEqualTo("encodedPassword");
    }

    @Test
    void shouldReturnEmptyWhenAccountNotFound() {
        // Given
        UUID nonExistentId = UUID.randomUUID();

        // When
        Optional<AccountInfo> foundAccount = accountInfoRepository.findById(nonExistentId);

        // Then
        assertThat(foundAccount).isEmpty();
    }

    @Test
    void shouldSaveAccount() {
        // Given
        UserInfo user = createTestUser();
        UserInfo savedUser = userInfoRepository.save(user);
        
        AccountInfo account = new AccountInfo(savedUser.getId(), "encodedPassword");

        // When
        AccountInfo savedAccount = accountInfoRepository.save(account);

        // Then
        assertThat(savedAccount.getId()).isEqualTo(savedUser.getId());
        assertThat(savedAccount.getPassword()).isEqualTo("encodedPassword");
    }

    private UserInfo createTestUser() {
        UserInfo user = new UserInfo();
        user.setFirstName("Test");
        user.setSecondName("User");
        user.setBirthdate(java.time.LocalDate.of(1999, 1, 1));
        return user;
    }
}
