package com.highload.architect.soc.network.dao;

import com.highload.architect.soc.network.model.AccountInfo;

import java.util.UUID;

public interface AccountInfoDao extends AbstractDao<AccountInfo> {
    AccountInfo getByUserInfoId(UUID userId);
}
