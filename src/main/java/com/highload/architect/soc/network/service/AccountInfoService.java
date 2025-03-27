package com.highload.architect.soc.network.service;

import com.highload.architect.soc.network.model.AccountInfo;

import java.util.UUID;

public interface AccountInfoService {

    AccountInfo getById(UUID userId);
}
