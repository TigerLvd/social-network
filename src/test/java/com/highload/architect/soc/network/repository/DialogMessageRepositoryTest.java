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
import java.util.ArrayList;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DialogMessageRepositoryTest {

    @Mock
    private DialogMessageRepository dialogMessageRepository;

    private final UUID user1Id = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");
    private final UUID user2Id = UUID.fromString("123e4567-e89b-12d3-a456-426614174001");

    @Test
    void save_ShouldSaveMessage() {
        // Arrange
        DialogMessageEntity message = createTestMessage(user1Id, user2Id, "Test message");
        DialogMessageEntity savedMessage = createTestMessage(user1Id, user2Id, "Test message");
        savedMessage.setId(UUID.randomUUID());

        when(dialogMessageRepository.save(message)).thenReturn(savedMessage);

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
        List<DialogMessageEntity> mockMessages = List.of(
            createTestMessage(user1Id, user2Id, "Message 1"),
            createTestMessage(user2Id, user1Id, "Message 2"),
            createTestMessage(user1Id, user2Id, "Message 3")
        );

        Page<DialogMessageEntity> mockPage = new PageImpl<>(mockMessages);
        Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "createdAt"));

        when(dialogMessageRepository.findByUsersPaged(eq(user1Id), eq(user2Id), any(Pageable.class)))
            .thenReturn(mockPage);

        // Act
        Page<DialogMessageEntity> result = dialogMessageRepository.findByUsersPaged(user1Id, user2Id, pageable);

        // Assert
        assertEquals(3, result.getTotalElements());
        assertEquals(3, result.getContent().size());
        assertEquals("Message 1", result.getContent().get(0).getText());
        assertEquals("Message 2", result.getContent().get(1).getText());
        assertEquals("Message 3", result.getContent().get(2).getText());
    }

    @Test
    void findByUsersPaged_ShouldReturnOnlyMessagesBetweenSpecifiedUsers() {
        // Arrange
        UUID user3Id = UUID.fromString("123e4567-e89b-12d3-a456-426614174002");

        List<DialogMessageEntity> mockMessages = List.of(
            createTestMessage(user1Id, user2Id, "Between user1 and user2")
        );

        Page<DialogMessageEntity> mockPage = new PageImpl<>(mockMessages);
        Pageable pageable = PageRequest.of(0, 10);

        when(dialogMessageRepository.findByUsersPaged(eq(user1Id), eq(user2Id), any(Pageable.class)))
            .thenReturn(mockPage);

        // Act
        Page<DialogMessageEntity> result = dialogMessageRepository.findByUsersPaged(user1Id, user2Id, pageable);

        // Assert
        assertEquals(1, result.getTotalElements());
        assertEquals("Between user1 and user2", result.getContent().get(0).getText());
    }

    @Test
    void findByUsersPaged_ShouldSupportPagination() {
        // Arrange
        List<DialogMessageEntity> mockMessages = List.of(
            createTestMessage(user1Id, user2Id, "Message 1"),
            createTestMessage(user1Id, user2Id, "Message 2")
        );

        Pageable pageable = PageRequest.of(0, 2, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<DialogMessageEntity> mockPage = new PageImpl<>(mockMessages, pageable, 5);

        when(dialogMessageRepository.findByUsersPaged(eq(user1Id), eq(user2Id), eq(pageable)))
            .thenReturn(mockPage);

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
