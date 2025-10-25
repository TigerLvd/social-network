package com.highload.architect.soc.network.repository;

import com.highload.architect.soc.network.BaseIntegrationTest;
import com.highload.architect.soc.network.model.SimpleToken;
import com.highload.architect.soc.network.model.UserInfo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class SimpleTokenRepositoryTest extends BaseIntegrationTest {

    @Autowired
    private SimpleTokenRepository simpleTokenRepository;

    @Autowired
    private UserInfoRepository userInfoRepository;

    @Test
    void shouldFindTokenById() {
        // Given
        UserInfo user = createTestUser();
        UserInfo savedUser = userInfoRepository.save(user);
        
        SimpleToken token = createTestToken(savedUser.getId());
        SimpleToken savedToken = simpleTokenRepository.save(token);

        // When
        Optional<SimpleToken> foundToken = simpleTokenRepository.findById(savedToken.getId());

        // Then
        assertThat(foundToken).isPresent();
        assertThat(foundToken.get().getUserId()).isEqualTo(savedUser.getId());
        assertThat(foundToken.get().getIssuedAt()).isNotNull();
        assertThat(foundToken.get().getExpiration()).isNotNull();
    }

    @Test
    void shouldReturnEmptyWhenTokenNotFound() {
        // Given
        UUID nonExistentId = UUID.randomUUID();

        // When
        Optional<SimpleToken> foundToken = simpleTokenRepository.findById(nonExistentId);

        // Then
        assertThat(foundToken).isEmpty();
    }

    @Test
    void shouldSaveToken() {
        // Given
        UserInfo user = createTestUser();
        UserInfo savedUser = userInfoRepository.save(user);
        
        SimpleToken token = createTestToken(savedUser.getId());

        // When
        SimpleToken savedToken = simpleTokenRepository.save(token);

        // Then
        assertThat(savedToken.getId()).isNotNull();
        assertThat(savedToken.getUserId()).isEqualTo(savedUser.getId());
        assertThat(savedToken.getIssuedAt()).isNotNull();
        assertThat(savedToken.getExpiration()).isNotNull();
    }

    private UserInfo createTestUser() {
        UserInfo user = new UserInfo();
        user.setFirstName("Test");
        user.setSecondName("User");
        user.setBirthdate(java.time.LocalDate.of(1999, 1, 1));
        return user;
    }

    private SimpleToken createTestToken(UUID userId) {
        SimpleToken token = new SimpleToken();
        token.setUserId(userId);
        token.setIssuedAt(LocalDateTime.now());
        token.setExpiration(LocalDateTime.now().plusDays(1));
        return token;
    }
}
