package com.highload.architect.soc.network.repository;

import com.highload.architect.soc.network.model.AccountInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.NativeQuery;

import java.util.UUID;

public interface AccountInfoRepository extends JpaRepository<AccountInfo, UUID> {
    @NativeQuery("select count(id)\n" +
            "from mydb.social_network.account_info\n" +
            "where id = :accountId\n" +
            "    and password = :password")
    Integer countByIdAndPassword(UUID accountId, String password);
}
