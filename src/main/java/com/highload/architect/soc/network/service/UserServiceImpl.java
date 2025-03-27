package com.highload.architect.soc.network.service;

import com.highload.architect.soc.network.mapper.UserInfoMapper;
import com.highload.architect.soc.network.model.AccountInfo;
import com.highload.architect.soc.network.model.User;
import com.highload.architect.soc.network.model.UserInfo;
import com.highload.architect.soc.network.model.UserRegisterPostRequest;
import com.highload.architect.soc.network.repository.AccountInfoRepository;
import com.highload.architect.soc.network.repository.UserInfoRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    private final AccountInfoRepository accountInfoRepository;
    private final PasswordEncoder encoder;
    private final UserInfoMapper userInfoMapper;
    private final UserInfoRepository userInfoRepository;

    public UserServiceImpl(AccountInfoRepository accountInfoRepository, UserInfoMapper userInfoMapper, UserInfoRepository userInfoRepository) {
        this.userInfoRepository = userInfoRepository;
        this.accountInfoRepository = accountInfoRepository;
        this.userInfoMapper = userInfoMapper;
        this.encoder = new BCryptPasswordEncoder();
    }

    public UserInfo getById(UUID userId) {
        return userInfoRepository.findUserInfoById(userId);
    }

    @Override
    public UUID create(UserRegisterPostRequest requestUserInfo) {
        UserInfo savedUserInfo = userInfoRepository.save(userInfoMapper.toEntity(requestUserInfo));
        String password = requestUserInfo.getPassword();
        UUID userInfoId = savedUserInfo.getId();
        AccountInfo accountInfo = new AccountInfo(userInfoId, encoder.encode(password));
        accountInfoRepository.save(accountInfo);

        return userInfoId;
    }

    @Override
    public User getUser(UUID userId) {
        UserInfo userInfo = getById(userId);
        if (userInfo == null) {
            return null;
        }
        return userInfoMapper.toDto(userInfo);
    }

    @Override
    public List<User> findByFirstNameAndSecondName(String firstName, String lastName) {
        return Optional.ofNullable(userInfoRepository.findByFirstNameAndSecondName(firstName, lastName))
                .orElse(Collections.emptyList())
                .stream()
                .map(userInfoMapper::toDto)
                .collect(Collectors.toList());
    }
}
