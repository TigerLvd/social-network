package com.highload.architect.soc.network.service;

import com.highload.architect.soc.network.model.User;
import com.highload.architect.soc.network.model.UserInfo;
import com.highload.architect.soc.network.model.UserRegisterPostRequest;

import java.util.List;
import java.util.UUID;

public interface UserService {
    UserInfo getById(UUID userId);

    UUID create(UserRegisterPostRequest userRegisterPostRequest);

    User getUser(UUID userId);

    List<User> findByFirstNameAndSecondName(String firstName, String lastName);
}
