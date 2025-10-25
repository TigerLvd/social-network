package com.highload.architect.soc.network.api;

import com.highload.architect.soc.network.exception.UserNotFoundException;
import com.highload.architect.soc.network.model.User;
import com.highload.architect.soc.network.model.UserRegisterPost200Response;
import com.highload.architect.soc.network.model.UserRegisterPostRequest;
import com.highload.architect.soc.network.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;

@RestController
public class UserApiImpl implements UserApi {
    private static final Logger log = LoggerFactory.getLogger(UserApiImpl.class);
    
    private final UserService userService;

    public UserApiImpl(UserService userService) {
        this.userService = userService;
    }

    @Override
    public ResponseEntity<User> userGetIdGet(String id) {
        log.debug("Getting user by ID: {}", id);
        
        if (id == null || id.trim().isEmpty()) {
            log.warn("User ID is null or empty");
            return ResponseEntity.badRequest().build();
        }
        
        try {
            UUID userId = UUID.fromString(id);
            User user = userService.getUser(userId);
            return ResponseEntity.ok(user);
        } catch (IllegalArgumentException e) {
            log.warn("Invalid UUID format: {}", id);
            return ResponseEntity.badRequest().build();
        } catch (UserNotFoundException e) {
            log.warn("User not found with ID: {}", id);
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            log.error("Error getting user with ID: {}", id, e);
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).build();
        }
    }

    @Override
    public ResponseEntity<UserRegisterPost200Response> userRegisterPost(@Valid UserRegisterPostRequest userRegisterPostRequest) {
        log.info("Registering new user: {} {}", 
                userRegisterPostRequest.getFirstName(), userRegisterPostRequest.getSecondName());
        
        try {
            UUID userId = userService.create(userRegisterPostRequest);
            
            UserRegisterPost200Response response = new UserRegisterPost200Response();
            response.setUserId(userId.toString());
            
            log.info("User registered successfully with ID: {}", userId);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error registering user", e);
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).build();
        }
    }

    @Override
    public ResponseEntity<List<User>> userSearchGet(String firstName, String lastName) {
        log.debug("Searching users by firstName: {} and lastName: {}", firstName, lastName);
        
        if (firstName == null || lastName == null || 
            firstName.trim().isEmpty() || lastName.trim().isEmpty()) {
            log.warn("Invalid search parameters: firstName={}, lastName={}", firstName, lastName);
            return ResponseEntity.badRequest().build();
        }

        try {
            List<User> users = userService.findByFirstNameAndSecondName(firstName, lastName);
            log.debug("Found {} users", users.size());
            return ResponseEntity.ok(users);
        } catch (Exception e) {
            log.error("Error searching users", e);
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).build();
        }
    }
}
