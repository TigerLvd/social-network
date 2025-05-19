package com.highload.architect.soc.network.dao;

import com.highload.architect.soc.network.model.UserInfo;

import java.util.List;

public interface UserInfoDao extends AbstractDao<UserInfo> {
    List<UserInfo> findByFirstNameAndSecondName(String firstName, String lastName);
}
