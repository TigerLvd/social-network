package com.highload.architect.soc.network.service.impl;

import com.highload.architect.soc.network.dao.UserInfoDao;
import com.highload.architect.soc.network.mapper.UserInfoMapper;
import com.highload.architect.soc.network.model.AccountInfo;
import com.highload.architect.soc.network.model.User;
import com.highload.architect.soc.network.model.UserInfo;
import com.highload.architect.soc.network.model.UserRegisterPostRequest;
import com.highload.architect.soc.network.service.AccountInfoService;
import com.highload.architect.soc.network.service.UserService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    private final AccountInfoService accountInfoService;
    private final PasswordEncoder encoder;
    private final UserInfoMapper userInfoMapper;
    private final UserInfoDao userInfoDao;

    public UserServiceImpl(AccountInfoService accountInfoService, UserInfoMapper userInfoMapper, UserInfoDao userInfoDao) {
        this.userInfoDao = userInfoDao;
        this.accountInfoService = accountInfoService;
        this.userInfoMapper = userInfoMapper;
        this.encoder = new BCryptPasswordEncoder();
    }

    @Transactional(readOnly = true)
    public UserInfo getById(UUID userId) {
        return userInfoDao.getById(userId);
    }

    @Transactional
    @Override
    public UUID create(UserRegisterPostRequest requestUserInfo) {
        UserInfo entity = userInfoMapper.toEntity(requestUserInfo);
        userInfoDao.save(entity);
        String password = requestUserInfo.getPassword();
        UUID userInfoId = entity.getId();
        AccountInfo accountInfo = new AccountInfo(userInfoId, encoder.encode(password));
        accountInfoService.save(accountInfo);

        return userInfoId;
    }

    @Transactional(readOnly = true)
    @Override
    public User getUser(UUID userId) {
        UserInfo userInfo = getById(userId);
        if (userInfo == null) {
            return null;
        }
        return userInfoMapper.toDto(userInfo);
    }

    @Transactional(readOnly = true)
    @Override
    public List<User> findByFirstNameAndSecondName(String firstName, String lastName) {
        return Optional.ofNullable(userInfoDao.findByFirstNameAndSecondName(firstName, lastName))
                .orElse(Collections.emptyList())
                .stream()
                .map(userInfoMapper::toDto)
                .collect(Collectors.toList());
    }
}
