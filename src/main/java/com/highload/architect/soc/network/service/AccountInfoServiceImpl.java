package com.highload.architect.soc.network.service;

import com.highload.architect.soc.network.exception.UserNotFoundException;
import com.highload.architect.soc.network.model.AccountInfo;
import com.highload.architect.soc.network.repository.AccountInfoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class AccountInfoServiceImpl implements AccountInfoService {
    private static final Logger log = LoggerFactory.getLogger(AccountInfoServiceImpl.class);
    
    private final AccountInfoRepository accountInfoRepository;

    public AccountInfoServiceImpl(AccountInfoRepository accountInfoRepository) {
        this.accountInfoRepository = accountInfoRepository;
    }

    @Override
    public AccountInfo getById(UUID userId) {
        log.debug("Getting account info by ID: {}", userId);
        return accountInfoRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("Account not found with ID: " + userId));
    }
}
