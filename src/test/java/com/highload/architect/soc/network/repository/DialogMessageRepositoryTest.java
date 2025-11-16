package com.highload.architect.soc.network.repository;

import com.highload.architect.soc.network.model.DialogMessageEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.TestPropertySource;
import org.springframework.boot.autoconfigure.domain.EntityScan;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@EntityScan(basePackages = "com.highload.architect.soc.network.model")
@TestPropertySource(properties = {
    "spring.datasource.url=jdbc:h2:mem:testdb",
    "spring.datasource.driver-class-name=org.h2.Driver",
    "spring.jpa.hibernate.ddl-auto=create-drop",
    "spring.jpa.database-platform=org.hibernate.dialect.H2Dialect",
    "spring.liquibase.enabled=false"
})
class DialogMessageRepositoryTest {

    @Autowired
    private DialogMessageRepository dialogMessageRepository;

    private final UUID user1Id = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");
    private final UUID user2Id = UUID.fromString("123e4567-e89b-12d3-a456-426614174001");

    @Test
    void save_ShouldSaveMessage() {
        // Arrange
        DialogMessageEntity message = createTestMessage(user1Id, user2Id, "Test message");

        // Act
        DialogMessageEntity result = dialogMessageRepository.save(message);

        // Assert
        assertNotNull(result.getId());
        assertEquals(user1Id, result.getFromUserId());
        assertEquals(user2Id, result.getToUserId());
        assertEquals("Test message", result.getText());
        assertNotNull(result.getCreatedAt());
    }

    @Test
    void findByUsersPaged_ShouldReturnMessagesBetweenUsers() {
        // Arrange
        dialogMessageRepository.save(createTestMessage(user1Id, user2Id, "Message 1"));
        dialogMessageRepository.save(createTestMessage(user2Id, user1Id, "Message 2"));
        dialogMessageRepository.save(createTestMessage(user1Id, user2Id, "Message 3"));

        Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "createdAt"));

        // Act
        Page<DialogMessageEntity> result = dialogMessageRepository.findByUsersPaged(user1Id, user2Id, pageable);

        // Assert
        assertEquals(3, result.getTotalElements());
        assertEquals(3, result.getContent().size());
        // Check that all expected messages are present (order may vary due to same timestamps)
        List<String> texts = result.getContent().stream()
            .map(DialogMessageEntity::getText)
            .collect(Collectors.toList());
        assertTrue(texts.contains("Message 1"));
        assertTrue(texts.contains("Message 2"));
        assertTrue(texts.contains("Message 3"));
    }

    @Test
    void findByUsersPaged_ShouldReturnOnlyMessagesBetweenSpecifiedUsers() {
        // Arrange
        UUID user3Id = UUID.fromString("123e4567-e89b-12d3-a456-426614174002");

        dialogMessageRepository.save(createTestMessage(user1Id, user2Id, "Between user1 and user2"));
        dialogMessageRepository.save(createTestMessage(user1Id, user3Id, "Between user1 and user3"));

        Pageable pageable = PageRequest.of(0, 10);

        // Act
        Page<DialogMessageEntity> result = dialogMessageRepository.findByUsersPaged(user1Id, user2Id, pageable);

        // Assert
        assertEquals(1, result.getTotalElements());
        assertEquals("Between user1 and user2", result.getContent().get(0).getText());
    }

    @Test
    void findByUsersPaged_ShouldSupportPagination() {
        // Arrange
        dialogMessageRepository.save(createTestMessage(user1Id, user2Id, "Message 1"));
        dialogMessageRepository.save(createTestMessage(user1Id, user2Id, "Message 2"));
        dialogMessageRepository.save(createTestMessage(user1Id, user2Id, "Message 3"));
        dialogMessageRepository.save(createTestMessage(user1Id, user2Id, "Message 4"));
        dialogMessageRepository.save(createTestMessage(user1Id, user2Id, "Message 5"));

        Pageable pageable = PageRequest.of(0, 2, Sort.by(Sort.Direction.DESC, "createdAt"));

        // Act
        Page<DialogMessageEntity> result = dialogMessageRepository.findByUsersPaged(user1Id, user2Id, pageable);

        // Assert
        assertEquals(5, result.getTotalElements());
        assertEquals(2, result.getContent().size());
        assertTrue(result.hasNext());
    }

    private DialogMessageEntity createTestMessage(UUID fromUserId, UUID toUserId, String text) {
        DialogMessageEntity message = new DialogMessageEntity();
        message.setFromUserId(fromUserId);
        message.setToUserId(toUserId);
        message.setText(text);
        message.setCreatedAt(LocalDateTime.now());
        return message;
    }
}
