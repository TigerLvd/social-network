package com.highload.architect.soc.network.repository;

import com.highload.architect.soc.network.model.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserInfoRepository extends JpaRepository<UserInfo, UUID> {
    
    Optional<UserInfo> findById(UUID userId);
    
    List<UserInfo> findByFirstNameAndSecondName(String firstName, String secondName);
}
