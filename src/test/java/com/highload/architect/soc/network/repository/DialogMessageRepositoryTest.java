package com.highload.architect.soc.network.repository;

import com.highload.architect.soc.network.model.DialogMessageEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
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
        DialogMessageEntity savedMessage = dialogMessageRepository.save(message);

        // Assert
        assertNotNull(savedMessage.getId());
        assertEquals(user1Id, savedMessage.getFromUserId());
        assertEquals(user2Id, savedMessage.getToUserId());
        assertEquals("Test message", savedMessage.getText());
        assertNotNull(savedMessage.getCreatedAt());
    }

    @Test
    void findByUsersPaged_ShouldReturnMessagesBetweenUsers() {
        // Arrange
        DialogMessageEntity message1 = createTestMessage(user1Id, user2Id, "Message 1");
        DialogMessageEntity message2 = createTestMessage(user2Id, user1Id, "Message 2");
        DialogMessageEntity message3 = createTestMessage(user1Id, user2Id, "Message 3");

        dialogMessageRepository.save(message1);
        dialogMessageRepository.save(message2);
        dialogMessageRepository.save(message3);

        Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "createdAt"));

        // Act
        Page<DialogMessageEntity> result = dialogMessageRepository.findByUsersPaged(user1Id, user2Id, pageable);

        // Assert
        assertEquals(3, result.getTotalElements());
        assertEquals(3, result.getContent().size());

        // Check sorting - newest first
        List<DialogMessageEntity> messages = result.getContent();
        assertTrue(messages.get(0).getCreatedAt().isAfter(messages.get(1).getCreatedAt()) ||
                  messages.get(0).getCreatedAt().equals(messages.get(1).getCreatedAt()));
    }

    @Test
    void findByUsersPaged_ShouldReturnOnlyMessagesBetweenSpecifiedUsers() {
        // Arrange
        UUID user3Id = UUID.fromString("123e4567-e89b-12d3-a456-426614174002");

        DialogMessageEntity message1 = createTestMessage(user1Id, user2Id, "Between user1 and user2");
        DialogMessageEntity message2 = createTestMessage(user1Id, user3Id, "Between user1 and user3");
        DialogMessageEntity message3 = createTestMessage(user2Id, user3Id, "Between user2 and user3");

        dialogMessageRepository.save(message1);
        dialogMessageRepository.save(message2);
        dialogMessageRepository.save(message3);

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
        for (int i = 1; i <= 5; i++) {
            DialogMessageEntity message = createTestMessage(user1Id, user2Id, "Message " + i);
            dialogMessageRepository.save(message);
        }

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
