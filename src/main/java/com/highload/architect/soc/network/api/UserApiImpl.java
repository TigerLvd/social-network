package com.highload.architect.soc.network.api;

import com.highload.architect.soc.network.model.User;
import com.highload.architect.soc.network.model.UserRegisterPost200Response;
import com.highload.architect.soc.network.model.UserRegisterPostRequest;
import com.highload.architect.soc.network.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
public class UserApiImpl implements UserApi {
    private final UserService userService;

    public UserApiImpl(UserService userService) {
        this.userService = userService;
    }

    @Override
    public ResponseEntity<User> userGetIdGet(String id) {
        User user = userService.getUser(UUID.fromString(id));
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<UserRegisterPost200Response> userRegisterPost(UserRegisterPostRequest userRegisterPostRequest) {
        UUID userId = userService.create(userRegisterPostRequest);

        UserRegisterPost200Response response = new UserRegisterPost200Response();
        response.setUserId(userId.toString());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<List<User>> userSearchGet(String firstName, String lastName) {
        List<User> users = userService.findByFirstNameAndSecondName(firstName, lastName);

        return new ResponseEntity<>(users, HttpStatus.OK);
    }
}
