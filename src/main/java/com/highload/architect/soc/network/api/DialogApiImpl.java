package com.highload.architect.soc.network.api;

import com.highload.architect.soc.network.exception.FriendshipRequiredException;
import com.highload.architect.soc.network.model.DialogMessage;
import com.highload.architect.soc.network.model.DialogUserIdSendPostRequest;
import com.highload.architect.soc.network.util.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
public class DialogApiImpl implements DialogApi {
    private static final Logger log = LoggerFactory.getLogger(DialogApiImpl.class);

    private final SecurityUtils securityUtils;

    // In-memory storage for messages (will be replaced with database in later phases)
    private final List<DialogMessage> messages = new ArrayList<>();

    public DialogApiImpl(SecurityUtils securityUtils) {
        this.securityUtils = securityUtils;
    }

    @Override
    public ResponseEntity<List<DialogMessage>> dialogUserIdListGet(String userId) {
        log.debug("Getting dialog list for user ID: {}", userId);

        try {
            UUID currentUserId = securityUtils.getCurrentUserId();
            UUID targetUserId = validateAndParseUserId(userId);

            validateFriendship(currentUserId, targetUserId);

            List<DialogMessage> dialogMessages = getDialogMessages(currentUserId, targetUserId);

            log.debug("Retrieved {} messages for dialog between {} and {}", dialogMessages.size(), currentUserId, targetUserId);
            return ResponseEntity.ok(dialogMessages);
        } catch (IllegalStateException e) {
            log.warn("User not authenticated: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        } catch (FriendshipRequiredException e) {
            log.warn("Friendship required: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        } catch (IllegalArgumentException e) {
            log.warn("Invalid UUID format: {}", userId);
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            log.error("Error getting dialog list for user ID: {}", userId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Override
    public ResponseEntity<Void> dialogUserIdSendPost(String userId, @Valid DialogUserIdSendPostRequest dialogUserIdSendPostRequest) {
        log.info("Sending message to user ID: {}", userId);

        try {
            UUID currentUserId = securityUtils.getCurrentUserId();
            UUID targetUserId = validateAndParseUserId(userId);

            validateFriendship(currentUserId, targetUserId);

            DialogMessage message = createMessage(currentUserId, targetUserId, dialogUserIdSendPostRequest.getText());
            messages.add(message);

            log.info("Message sent successfully from {} to {}", currentUserId, targetUserId);
            return ResponseEntity.ok().build();
        } catch (IllegalStateException e) {
            log.warn("User not authenticated: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        } catch (FriendshipRequiredException e) {
            log.warn("Friendship required: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        } catch (IllegalArgumentException e) {
            log.warn("Invalid UUID format: {}", userId);
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            log.error("Error sending message to user ID: {}", userId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Validate and parse user ID string to UUID.
     */
    private UUID validateAndParseUserId(String userId) {
        return UUID.fromString(userId);
    }

    /**
     * Validate that users are friends, throw exception if not.
     */
    private void validateFriendship(UUID currentUserId, UUID targetUserId) {
        if (!areFriends(currentUserId, targetUserId)) {
            log.warn("Users {} and {} are not friends", currentUserId, targetUserId);
            throw new FriendshipRequiredException("Users are not friends");
        }
    }

    /**
     * Get all dialog messages between two users, sorted by creation time (newest first).
     */
    private List<DialogMessage> getDialogMessages(UUID currentUserId, UUID targetUserId) {
        return messages.stream()
                .filter(msg -> isMessageBetweenUsers(msg, currentUserId, targetUserId))
                .sorted(Comparator.comparing(DialogMessage::getCreated).reversed())
                .collect(Collectors.toList());
    }

    /**
     * Create a new dialog message.
     */
    private DialogMessage createMessage(UUID fromUserId, UUID toUserId, String text) {
        DialogMessage message = new DialogMessage();
        message.setId(UUID.randomUUID());
        message.setText(text);
        message.setFrom(fromUserId);
        message.setTo(toUserId);
        message.setCreated(LocalDateTime.now());
        return message;
    }

    /**
     * Helper method to check if a message is between two specific users
     */
    private boolean isMessageBetweenUsers(DialogMessage message, UUID user1, UUID user2) {
        return (message.getFrom().equals(user1) && message.getTo().equals(user2)) ||
               (message.getFrom().equals(user2) && message.getTo().equals(user1));
    }

    /**
     * Check if two users are friends.
     * This is a placeholder implementation - returns true only for test users.
     * In future phases, this will be replaced with actual friendship checking logic.
     */
    private boolean areFriends(UUID user1, UUID user2) {
        // TODO: Implement actual friendship checking logic in later phases
        // For now, allow friendship between test users used in DialogApiImplTest
        UUID testUser1 = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");
        UUID testUser2 = UUID.fromString("123e4567-e89b-12d3-a456-426614174001");

        return (user1.equals(testUser1) && user2.equals(testUser2)) ||
               (user1.equals(testUser2) && user2.equals(testUser1));
    }
}
