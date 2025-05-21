package com.highload.architect.soc.network.service.impl;

import com.highload.architect.soc.network.dao.AccountInfoDao;
import com.highload.architect.soc.network.model.AccountInfo;
import com.highload.architect.soc.network.service.AccountInfoService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class AccountInfoServiceImpl implements AccountInfoService {
    private final AccountInfoDao accountInfoDao;

    public AccountInfoServiceImpl(AccountInfoDao accountInfoDao) {
        this.accountInfoDao = accountInfoDao;
    }

    @Transactional(readOnly = true)
    @Override
    public AccountInfo getById(UUID id) {
        return accountInfoDao.getById(id);
    }

    @Transactional
    @Override
    public void save(AccountInfo accountInfo) {
        accountInfoDao.save(accountInfo);
    }
}
