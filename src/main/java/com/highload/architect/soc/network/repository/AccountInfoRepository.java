package com.highload.architect.soc.network.repository;

import com.highload.architect.soc.network.model.AccountInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface AccountInfoRepository extends JpaRepository<AccountInfo, UUID> {

    Optional<AccountInfo> findById(UUID accountId);
}
