package com.highload.architect.soc.network.repository;

import com.highload.architect.soc.network.model.SimpleToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.NativeQuery;

import java.util.UUID;

public interface SimpleTokenRepository extends JpaRepository<SimpleToken, UUID> {
    @NativeQuery("select * " +
            "from mydb.social_network.simple_token " +
            "where id = :id")
    SimpleToken getSimpleTokenById(UUID id);
}
