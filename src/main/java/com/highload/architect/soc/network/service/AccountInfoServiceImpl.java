package com.highload.architect.soc.network.service;

import com.highload.architect.soc.network.model.AccountInfo;
import com.highload.architect.soc.network.repository.AccountInfoRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class AccountInfoServiceImpl implements AccountInfoService {
    private final AccountInfoRepository accountInfoRepository;

    public AccountInfoServiceImpl(AccountInfoRepository accountInfoRepository) {
        this.accountInfoRepository = accountInfoRepository;
    }

    @Override
    public AccountInfo getById(UUID userId) {
        return accountInfoRepository.getAccountInfoById(userId);
    }
}
