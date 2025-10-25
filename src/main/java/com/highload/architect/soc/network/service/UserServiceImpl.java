package com.highload.architect.soc.network.service;

import com.highload.architect.soc.network.exception.UserNotFoundException;
import com.highload.architect.soc.network.mapper.UserInfoMapper;
import com.highload.architect.soc.network.model.AccountInfo;
import com.highload.architect.soc.network.model.User;
import com.highload.architect.soc.network.model.UserInfo;
import com.highload.architect.soc.network.model.UserRegisterPostRequest;
import com.highload.architect.soc.network.repository.AccountInfoRepository;
import com.highload.architect.soc.network.repository.UserInfoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);
    
    private final AccountInfoRepository accountInfoRepository;
    private final PasswordEncoder encoder;
    private final UserInfoMapper userInfoMapper;
    private final UserInfoRepository userInfoRepository;

    public UserServiceImpl(AccountInfoRepository accountInfoRepository, 
                          UserInfoMapper userInfoMapper, 
                          UserInfoRepository userInfoRepository,
                          PasswordEncoder encoder) {
        this.userInfoRepository = userInfoRepository;
        this.accountInfoRepository = accountInfoRepository;
        this.userInfoMapper = userInfoMapper;
        this.encoder = encoder;
    }

    public UserInfo getById(UUID userId) {
        log.debug("Getting user info by ID: {}", userId);
        return userInfoRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + userId));
    }

    @Override
    public UUID create(UserRegisterPostRequest requestUserInfo) {
        log.info("Creating new user with firstName: {} and lastName: {}", 
                requestUserInfo.getFirstName(), requestUserInfo.getSecondName());
        
        UserInfo savedUserInfo = userInfoRepository.save(userInfoMapper.toEntity(requestUserInfo));
        String password = requestUserInfo.getPassword();
        UUID userInfoId = savedUserInfo.getId();
        AccountInfo accountInfo = new AccountInfo(userInfoId, encoder.encode(password));
        accountInfoRepository.save(accountInfo);

        log.info("User created successfully with ID: {}", userInfoId);
        return userInfoId;
    }

    @Override
    public User getUser(UUID userId) {
        log.debug("Getting user by ID: {}", userId);
        UserInfo userInfo = getById(userId);
        return userInfoMapper.toDto(userInfo);
    }

    @Override
    public List<User> findByFirstNameAndSecondName(String firstName, String lastName) {
        log.debug("Searching users by firstName: {} and lastName: {}", firstName, lastName);
        return Optional.ofNullable(userInfoRepository.findByFirstNameAndSecondName(firstName, lastName))
                .orElse(Collections.emptyList())
                .stream()
                .map(userInfoMapper::toDto)
                .collect(Collectors.toList());
    }
}
