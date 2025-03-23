package com.highload.architect.soc.network.service;

import com.highload.architect.soc.network.model.AccountInfo;
import com.highload.architect.soc.network.model.User;
import com.highload.architect.soc.network.model.UserInfo;
import com.highload.architect.soc.network.model.UserRegisterPostRequest;
import com.highload.architect.soc.network.repository.AccountInfoRepository;
import com.highload.architect.soc.network.repository.UserInfoRepository;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    private final UserInfoRepository userInfoRepository;
    private final AccountInfoRepository accountInfoRepository;

    public UserServiceImpl(UserInfoRepository userInfoRepository, AccountInfoRepository accountInfoRepository) {
        this.userInfoRepository = userInfoRepository;
        this.accountInfoRepository = accountInfoRepository;
    }

    public UserInfo getById(UUID userId) {
        return userInfoRepository.getUserInfoById(userId);
    }

    @Override
    public UUID create(UserRegisterPostRequest requestUserInfo) {
        UserInfo savedUserInfo = userInfoRepository.save(buildUserInfo(requestUserInfo));
        accountInfoRepository.save(buildAccountInfo(savedUserInfo.getId(), requestUserInfo.getPassword()));

        return savedUserInfo.getId();
    }

    private UserInfo buildUserInfo(UserRegisterPostRequest userInfo) {
        UserInfo user = new UserInfo();
        user.setBirthdate(userInfo.getBirthdate());
        user.setBiography(userInfo.getBiography());
        user.setCity(userInfo.getCity());
        user.setFirstName(userInfo.getFirstName());
        user.setSecondName(userInfo.getSecondName());
        return user;
    }

    private AccountInfo buildAccountInfo(UUID accountId, String password) {
        AccountInfo accountInfo = new AccountInfo();
        accountInfo.setId(accountId);
        accountInfo.setPassword(password);
        return accountInfo;
    }

    @Override
    public User getUser(UUID userId) {
        UserInfo userInfo = getById(userId);
        if (userInfo == null) {
            throw new RuntimeException("user not found");
        }
        return buildUser(userInfo);
    }

    @Override
    public List<User> findByFirstNameAndSecondName(String firstName, String lastName) {
        return Optional.ofNullable(userInfoRepository.findByFirstNameAndSecondName(firstName, lastName))
                .orElse(Collections.emptyList())
                .stream()
                .map(this::convetToUser)
                .collect(Collectors.toList());
    }

    private User convetToUser(UserInfo userInfo) {
        User user = new User();
        user.setId(userInfo.getId().toString());
        user.setBiography(userInfo.getBiography());
        user.setFirstName(userInfo.getFirstName());
        user.setSecondName(userInfo.getSecondName());
        user.setBirthdate(userInfo.getBirthdate());
        user.setCity(userInfo.getCity());
        return user;
    }

    private User buildUser(UserInfo userInfo) {
        User user = new User();
        user.setId(userInfo.getId().toString());
        user.setBirthdate(userInfo.getBirthdate());
        user.setBiography(userInfo.getBiography());
        user.setCity(userInfo.getCity());
        user.setFirstName(userInfo.getFirstName());
        user.setSecondName(userInfo.getSecondName());
        return user;
    }

    public List<UserInfo> getAllUsers() {
        return userInfoRepository.findAll();
    }

    public UserInfo createUser(UserInfo user) {
        return userInfoRepository.save(user);
    }
}
