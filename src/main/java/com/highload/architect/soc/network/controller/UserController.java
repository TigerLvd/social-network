package com.highload.architect.soc.network.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
@Tag(name = "User Controller", description = "API для управления пользователями")
public class UserController {
    @Operation(summary = "Логин пользователя", description = "Метод для аутентификации пользователя")
    @PostMapping("/login")
    public boolean login() {
        return true;
    }
}
