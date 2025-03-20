package com.highload.architect.soc.network.api;

import com.highload.architect.soc.network.model.User;
import com.highload.architect.soc.network.model.UserRegisterPost200Response;
import com.highload.architect.soc.network.model.UserRegisterPostRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
public class UserApiImpl implements UserApi {
    @Override
    public ResponseEntity<User> userGetIdGet(String id) {
        User user = new User();
        user.setId(id);
        user.setBirthdate(LocalDate.now());
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<UserRegisterPost200Response> userRegisterPost(UserRegisterPostRequest userRegisterPostRequest) {
        UserRegisterPost200Response response = new UserRegisterPost200Response();
        response.setUserId(String.valueOf(123));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<List<User>> userSearchGet(String firstName, String lastName) {
        User user1 = new User();
        user1.setId(String.valueOf(1));
        User user2 = new User();
        user2.setId(String.valueOf(1));
        User user3 = new User();
        user3.setId(String.valueOf(1));
        return new ResponseEntity<>(List.of(user1, user2, user3), HttpStatus.OK);
    }
}
