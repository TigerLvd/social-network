package com.highload.architect.soc.network.service;

import com.highload.architect.soc.network.model.SimpleToken;

import java.util.UUID;

public interface SimpleTokenService {

    SimpleToken getSimpleTokenById(UUID id);

    SimpleToken createSimpleToken(UUID userInfoId);
}
