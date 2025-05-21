package com.highload.architect.soc.network.dao.impl;

import com.highload.architect.soc.network.dao.AccountInfoDao;
import com.highload.architect.soc.network.model.AccountInfo;
import org.springframework.stereotype.Service;

@Service
public class AccountInfoDaoImpl extends AbstractDaoImpl<AccountInfo> implements AccountInfoDao {
    @Override
    protected Class<AccountInfo> getEntityClass() {
        return AccountInfo.class;
    }
}
