package com.highload.architect.soc.network.security;

import com.highload.architect.soc.network.model.SimpleToken;
import com.highload.architect.soc.network.service.SimpleTokenService;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.UUID;

@Component
public class SimpleTokenProvider {
    public static final String HEADER = "Authorization";
    public static final String HEADER_PREFIX = "Bearer ";

    static {
        LoggerFactory.getLogger(SimpleTokenProvider.class);
    }

    private final SimpleTokenService simpleTokenService;

    public SimpleTokenProvider(SimpleTokenService simpleTokenService) {
        this.simpleTokenService = simpleTokenService;
    }

    public SimpleToken getTokenFromRequest(final HttpServletRequest request) {
        String header = request.getHeader(HEADER);
        if (org.apache.commons.lang3.StringUtils.isBlank(header)) {
            throw new AuthenticationServiceException("Authorization header cannot be blank!");
        }
        if (header.length() < HEADER_PREFIX.length()) {
            throw new AuthenticationServiceException("Invalid authorization header size.");
        }
        String token = header.substring(HEADER_PREFIX.length());

        SimpleToken simpleToken = simpleTokenService.getSimpleTokenById(UUID.fromString(token));
        if (simpleToken == null) {
            throw new AuthenticationServiceException("Invalid authorization header.");
        }
        LocalDateTime now = LocalDateTime.now();
        if (!simpleToken.getIssuedAt().isBefore(now)
                || !simpleToken.getExpiration().isAfter(now)) {
            throw new AuthenticationServiceException("Invalid authorization token header.");
        }
        return simpleToken;
    }
}
