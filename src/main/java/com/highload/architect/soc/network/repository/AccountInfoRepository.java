package com.highload.architect.soc.network.repository;

import com.highload.architect.soc.network.model.AccountInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AccountInfoRepository extends JpaRepository<AccountInfo, UUID> {
    AccountInfo findByIdAndPassword(UUID accountId, String password);
}
