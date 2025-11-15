package com.highload.architect.soc.network.api;

import com.highload.architect.soc.network.model.DialogMessage;
import com.highload.architect.soc.network.model.DialogUserIdSendPostRequest;
import com.highload.architect.soc.network.util.SecurityUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DialogApiImplTest {

    @Mock
    private SecurityUtils securityUtils;

    private DialogApiImpl dialogApi;

    private final UUID currentUserId = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");
    private final UUID targetUserId = UUID.fromString("123e4567-e89b-12d3-a456-426614174001");
    private final UUID nonFriendUserId = UUID.fromString("123e4567-e89b-12d3-a456-426614174002");

    @BeforeEach
    void setUp() {
        dialogApi = new DialogApiImpl(securityUtils);
    }

    @Test
    void dialogUserIdSendPost_ValidRequest_Returns200() {
        // Arrange
        when(securityUtils.getCurrentUserId()).thenReturn(currentUserId);
        DialogUserIdSendPostRequest request = new DialogUserIdSendPostRequest("Hello World");

        // Act
        ResponseEntity<Void> response = dialogApi.dialogUserIdSendPost(targetUserId.toString(), request);

        // Assert
        assertEquals(200, response.getStatusCode().value());
    }

    @Test
    void dialogUserIdListGet_ValidRequest_Returns200WithEmptyList() {
        // Arrange
        when(securityUtils.getCurrentUserId()).thenReturn(currentUserId);

        // Act
        ResponseEntity<List<DialogMessage>> response = dialogApi.dialogUserIdListGet(targetUserId.toString());

        // Assert
        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().isEmpty());
    }

    @Test
    void sendMessageAndRetrieve_ShouldReturnMessageInList() {
        // Arrange
        when(securityUtils.getCurrentUserId()).thenReturn(currentUserId);
        DialogUserIdSendPostRequest request = new DialogUserIdSendPostRequest("Test message");
        dialogApi.dialogUserIdSendPost(targetUserId.toString(), request);

        // Act - retrieve messages
        ResponseEntity<List<DialogMessage>> response = dialogApi.dialogUserIdListGet(targetUserId.toString());

        // Assert
        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        assertEquals("Test message", response.getBody().get(0).getText());
        assertEquals(currentUserId, response.getBody().get(0).getFrom());
        assertEquals(targetUserId, response.getBody().get(0).getTo());
    }

    @Test
    void dialogUserIdSendPost_InvalidUuid_Returns400() {
        // Arrange
        DialogUserIdSendPostRequest request = new DialogUserIdSendPostRequest("Hello World");

        // Act
        ResponseEntity<Void> response = dialogApi.dialogUserIdSendPost("invalid-uuid", request);

        // Assert
        assertEquals(400, response.getStatusCode().value());
    }

    @Test
    void dialogUserIdListGet_InvalidUuid_Returns400() {
        // Act
        ResponseEntity<List<DialogMessage>> response = dialogApi.dialogUserIdListGet("invalid-uuid");

        // Assert
        assertEquals(400, response.getStatusCode().value());
    }

    @Test
    void dialogUserIdSendPost_UnauthenticatedUser_Returns401() {
        // Arrange - SecurityUtils throws exception when user is not authenticated
        when(securityUtils.getCurrentUserId()).thenThrow(new IllegalStateException("Пользователь не аутентифицирован"));
        DialogUserIdSendPostRequest request = new DialogUserIdSendPostRequest("Hello World");

        // Act
        ResponseEntity<Void> response = dialogApi.dialogUserIdSendPost(targetUserId.toString(), request);

        // Assert - should return 401 (Unauthorized)
        // This test should fail (Red phase) - implementation doesn't handle authentication errors yet
        assertEquals(401, response.getStatusCode().value()); // Expected 401, but currently returns 500
    }

    @Test
    void dialogUserIdListGet_UnauthenticatedUser_Returns401() {
        // Arrange - SecurityUtils throws exception when user is not authenticated
        when(securityUtils.getCurrentUserId()).thenThrow(new IllegalStateException("Пользователь не аутентифицирован"));

        // Act
        ResponseEntity<List<DialogMessage>> response = dialogApi.dialogUserIdListGet(targetUserId.toString());

        // Assert - should return 401 (Unauthorized)
        // This test should fail (Red phase) - implementation doesn't handle authentication errors yet
        assertEquals(401, response.getStatusCode().value()); // Expected 401, but currently returns 500
    }

    @Test
    void dialogUserIdSendPost_NoFriendship_Returns403() {
        // Arrange
        when(securityUtils.getCurrentUserId()).thenReturn(currentUserId);
        DialogUserIdSendPostRequest request = new DialogUserIdSendPostRequest("Hello World");

        // Act - try to send message to a non-friend user
        ResponseEntity<Void> response = dialogApi.dialogUserIdSendPost(nonFriendUserId.toString(), request);

        // Assert - should return 403 (Forbidden) when users are not friends
        assertEquals(403, response.getStatusCode().value());
    }

    @Test
    void dialogUserIdListGet_NoFriendship_Returns403() {
        // Arrange
        when(securityUtils.getCurrentUserId()).thenReturn(currentUserId);

        // Act - try to get messages from a non-friend user
        ResponseEntity<List<DialogMessage>> response = dialogApi.dialogUserIdListGet(nonFriendUserId.toString());

        // Assert - should return 403 (Forbidden) when users are not friends
        assertEquals(403, response.getStatusCode().value());
    }
}
