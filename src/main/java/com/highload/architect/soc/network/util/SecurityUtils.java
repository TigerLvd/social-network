package com.highload.architect.soc.network.util;

import com.highload.architect.soc.network.service.impl.UserDetailsImpl;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * Утилитный класс для работы с SecurityContext.
 * Предоставляет методы для получения информации о текущем аутентифицированном пользователе.
 */
@Component
public class SecurityUtils {

    /**
     * Получает ID текущего аутентифицированного пользователя.
     *
     * @return UUID текущего пользователя
     * @throws IllegalStateException если пользователь не аутентифицирован
     */
    public UUID getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new IllegalStateException("Пользователь не аутентифицирован");
        }

        Object principal = authentication.getPrincipal();
        if (!(principal instanceof UserDetailsImpl userDetails)) {
            throw new IllegalStateException("Некорректный тип principal в SecurityContext");
        }

        return userDetails.getId();
    }

    /**
     * Получает объект UserDetailsImpl текущего аутентифицированного пользователя.
     *
     * @return UserDetailsImpl текущего пользователя
     * @throws IllegalStateException если пользователь не аутентифицирован
     */
    public static UserDetailsImpl getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new IllegalStateException("Пользователь не аутентифицирован");
        }

        Object principal = authentication.getPrincipal();
        if (!(principal instanceof UserDetailsImpl userDetails)) {
            throw new IllegalStateException("Некорректный тип principal в SecurityContext");
        }

        return userDetails;
    }
}
