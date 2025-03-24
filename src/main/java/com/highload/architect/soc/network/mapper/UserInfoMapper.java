package com.highload.architect.soc.network.mapper;

import com.highload.architect.soc.network.model.User;
import com.highload.architect.soc.network.model.UserInfo;
import com.highload.architect.soc.network.model.UserRegisterPostRequest;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserInfoMapper {
    User toDto(UserInfo userInfo);

    UserInfo toEntity(UserRegisterPostRequest requestUserInfo);
}
