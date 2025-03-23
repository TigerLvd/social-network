package com.highload.architect.soc.network.repository;

import com.highload.architect.soc.network.model.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.NativeQuery;

import java.util.List;
import java.util.UUID;

public interface UserInfoRepository extends JpaRepository<UserInfo, UUID> {
    @NativeQuery("select * from mydb.social_network.user_info where id = :userId")
    UserInfo findUserInfoById(UUID userId);

    @NativeQuery("select * from mydb.social_network.user_info where first_name = :firstName and second_name = :secondName")
    List<UserInfo> findByFirstNameAndSecondName(String firstName, String secondName);
}
