package com.highload.architect.soc.network.service;

import com.highload.architect.soc.network.model.SimpleToken;
import com.highload.architect.soc.network.repository.SimpleTokenRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class SimpleTokenServiceImpl implements SimpleTokenService {
    private final SimpleTokenRepository simpleTokenRepository;

    public SimpleTokenServiceImpl(SimpleTokenRepository simpleTokenRepository) {
        this.simpleTokenRepository = simpleTokenRepository;
    }

    @Override
    public SimpleToken getSimpleTokenById(UUID id) {
        return simpleTokenRepository.getSimpleTokenById(id);
    }

    @Override
    public SimpleToken createSimpleToken(UUID userInfoId) {
        SimpleToken simpleToken = new SimpleToken();
        simpleToken.setIssuedAt(LocalDateTime.now());
        simpleToken.setExpiration(LocalDateTime.now().plusDays(1));
        simpleToken.setUserId(userInfoId);

        simpleTokenRepository.save(simpleToken);
        return simpleToken;
    }
}
