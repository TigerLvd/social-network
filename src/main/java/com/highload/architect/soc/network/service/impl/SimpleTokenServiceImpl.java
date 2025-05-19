package com.highload.architect.soc.network.service.impl;

import com.highload.architect.soc.network.dao.SimpleTokenDao;
import com.highload.architect.soc.network.model.SimpleToken;
import com.highload.architect.soc.network.service.SimpleTokenService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class SimpleTokenServiceImpl implements SimpleTokenService {
    private final SimpleTokenDao simpleTokenDao;

    public SimpleTokenServiceImpl(SimpleTokenDao simpleTokenDao) {
        this.simpleTokenDao = simpleTokenDao;
    }

    @Transactional(readOnly = true)
    @Override
    public SimpleToken getSimpleTokenById(UUID id) {
        return simpleTokenDao.getById(id);
    }

    @Transactional
    @Override
    public SimpleToken createSimpleToken(UUID userInfoId) {
        SimpleToken simpleToken = new SimpleToken();
        simpleToken.setIssuedAt(LocalDateTime.now());
        simpleToken.setExpiration(LocalDateTime.now().plusDays(1));
        simpleToken.setUserId(userInfoId);

        simpleTokenDao.save(simpleToken);
        return simpleToken;
    }
}
