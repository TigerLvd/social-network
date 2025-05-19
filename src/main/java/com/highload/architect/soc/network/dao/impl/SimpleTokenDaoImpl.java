package com.highload.architect.soc.network.dao.impl;

import com.highload.architect.soc.network.dao.SimpleTokenDao;
import com.highload.architect.soc.network.model.SimpleToken;
import org.springframework.stereotype.Service;

@Service
public class SimpleTokenDaoImpl extends AbstractDaoImpl<SimpleToken> implements SimpleTokenDao {
    @Override
    protected Class<SimpleToken> getEntityClass() {
        return SimpleToken.class;
    }
}
