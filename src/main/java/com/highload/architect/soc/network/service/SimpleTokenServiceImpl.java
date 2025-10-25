package com.highload.architect.soc.network.service;

import com.highload.architect.soc.network.constants.SecurityConstants;
import com.highload.architect.soc.network.exception.TokenExpiredException;
import com.highload.architect.soc.network.model.SimpleToken;
import com.highload.architect.soc.network.repository.SimpleTokenRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class SimpleTokenServiceImpl implements SimpleTokenService {
    private static final Logger log = LoggerFactory.getLogger(SimpleTokenServiceImpl.class);
    
    private final SimpleTokenRepository simpleTokenRepository;

    public SimpleTokenServiceImpl(SimpleTokenRepository simpleTokenRepository) {
        this.simpleTokenRepository = simpleTokenRepository;
    }

    @Override
    public SimpleToken getSimpleTokenById(UUID id) {
        log.debug("Getting simple token by ID: {}", id);
        return simpleTokenRepository.findById(id)
                .orElseThrow(() -> new TokenExpiredException("Token not found with ID: " + id));
    }

    @Override
    public SimpleToken createSimpleToken(UUID userInfoId) {
        log.info("Creating simple token for user ID: {}", userInfoId);
        
        SimpleToken simpleToken = new SimpleToken();
        simpleToken.setIssuedAt(LocalDateTime.now());
        simpleToken.setExpiration(LocalDateTime.now().plusDays(SecurityConstants.TOKEN_EXPIRATION_DAYS));
        simpleToken.setUserId(userInfoId);

        SimpleToken savedToken = simpleTokenRepository.save(simpleToken);
        log.info("Simple token created successfully with ID: {}", savedToken.getId());
        return savedToken;
    }
}
