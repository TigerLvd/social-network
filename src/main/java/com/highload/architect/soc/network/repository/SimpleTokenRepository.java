package com.highload.architect.soc.network.repository;

import com.highload.architect.soc.network.model.SimpleToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface SimpleTokenRepository extends JpaRepository<SimpleToken, UUID> {
    
    Optional<SimpleToken> findById(UUID id);
}
