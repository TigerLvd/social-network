package com.highload.architect.soc.network.mapper;

import com.highload.architect.soc.network.model.AccountInfo;
import com.highload.architect.soc.network.model.UserInfo;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface AccountInfoMapper {
    AccountInfo convert(UserInfo userInfo);
}
