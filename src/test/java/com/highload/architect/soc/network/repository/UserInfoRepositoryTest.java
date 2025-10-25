package com.highload.architect.soc.network.repository;

import com.highload.architect.soc.network.BaseIntegrationTest;
import com.highload.architect.soc.network.model.UserInfo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class UserInfoRepositoryTest extends BaseIntegrationTest {

    @Autowired
    private UserInfoRepository userInfoRepository;

    @Test
    void shouldFindUserById() {
        // Given
        UserInfo user = new UserInfo();
        user.setFirstName("John");
        user.setSecondName("Doe");
        user.setBirthdate(java.time.LocalDate.of(1994, 1, 1));
        user.setBiography("Test biography");
        user.setCity("Test City");
        
        UserInfo savedUser = userInfoRepository.save(user);
        UUID userId = savedUser.getId();

        // When
        Optional<UserInfo> foundUser = userInfoRepository.findById(userId);

        // Then
        assertThat(foundUser).isPresent();
        assertThat(foundUser.get().getFirstName()).isEqualTo("John");
        assertThat(foundUser.get().getSecondName()).isEqualTo("Doe");
        assertThat(foundUser.get().getBirthdate()).isEqualTo(java.time.LocalDate.of(1994, 1, 1));
    }

    @Test
    void shouldReturnEmptyWhenUserNotFound() {
        // Given
        UUID nonExistentId = UUID.randomUUID();

        // When
        Optional<UserInfo> foundUser = userInfoRepository.findById(nonExistentId);

        // Then
        assertThat(foundUser).isEmpty();
    }

    @Test
    void shouldFindUsersByFirstNameAndSecondName() {
        // Given
        UserInfo user1 = new UserInfo();
        user1.setFirstName("John");
        user1.setSecondName("Doe");
        user1.setBirthdate(java.time.LocalDate.of(1994, 1, 1));
        userInfoRepository.save(user1);

        UserInfo user2 = new UserInfo();
        user2.setFirstName("John");
        user2.setSecondName("Smith");
        user2.setBirthdate(java.time.LocalDate.of(1999, 1, 1));
        userInfoRepository.save(user2);

        UserInfo user3 = new UserInfo();
        user3.setFirstName("Jane");
        user3.setSecondName("Doe");
        user3.setBirthdate(java.time.LocalDate.of(1996, 1, 1));
        userInfoRepository.save(user3);

        // When
        List<UserInfo> foundUsers = userInfoRepository.findByFirstNameAndSecondName("John", "Doe");

        // Then
        assertThat(foundUsers).hasSize(1);
        assertThat(foundUsers.get(0).getFirstName()).isEqualTo("John");
        assertThat(foundUsers.get(0).getSecondName()).isEqualTo("Doe");
    }

    @Test
    void shouldReturnEmptyListWhenNoUsersFound() {
        // When
        List<UserInfo> foundUsers = userInfoRepository.findByFirstNameAndSecondName("NonExistent", "User");

        // Then
        assertThat(foundUsers).isEmpty();
    }

    @Test
    void shouldSaveUser() {
        // Given
        UserInfo user = new UserInfo();
        user.setFirstName("Test");
        user.setSecondName("User");
        user.setBirthdate(java.time.LocalDate.of(1999, 1, 1));
        user.setBiography("Test biography");
        user.setCity("Test City");

        // When
        UserInfo savedUser = userInfoRepository.save(user);

        // Then
        assertThat(savedUser.getId()).isNotNull();
        assertThat(savedUser.getFirstName()).isEqualTo("Test");
        assertThat(savedUser.getSecondName()).isEqualTo("User");
    }
}
