package com.highload.architect.soc.network.exception;

/**
 * Исключение, выбрасываемое когда пользователи не являются друзьями,
 * но пытаются выполнить действия, требующие дружеской связи.
 */
public class FriendshipRequiredException extends RuntimeException {

    public FriendshipRequiredException(String message) {
        super(message);
    }

    public FriendshipRequiredException(String message, Throwable cause) {
        super(message, cause);
    }
}
