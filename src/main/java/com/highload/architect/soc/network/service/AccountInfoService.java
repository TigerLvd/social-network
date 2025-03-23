package com.highload.architect.soc.network.service;

public interface AccountInfoService {

    boolean existByUserIdAndPassword(String id, String password);
}
