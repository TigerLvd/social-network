package com.highload.architect.soc.network.mapper;

import com.highload.architect.soc.network.model.User;
import com.highload.architect.soc.network.model.UserInfo;
import com.highload.architect.soc.network.model.UserRegisterPostRequest;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-10-25T11:33:40+0500",
    comments = "version: 1.6.0, compiler: Eclipse JDT (IDE) 3.44.0.v20251001-1143, environment: Java 21.0.8 (Eclipse Adoptium)"
)
@Component
public class UserInfoMapperImpl implements UserInfoMapper {

    @Override
    public User toDto(UserInfo userInfo) {
        if ( userInfo == null ) {
            return null;
        }

        User user = new User();

        if ( userInfo.getId() != null ) {
            user.setId( userInfo.getId().toString() );
        }
        user.setFirstName( userInfo.getFirstName() );
        user.setSecondName( userInfo.getSecondName() );
        user.setBirthdate( userInfo.getBirthdate() );
        user.setBiography( userInfo.getBiography() );
        user.setCity( userInfo.getCity() );

        return user;
    }

    @Override
    public UserInfo toEntity(UserRegisterPostRequest requestUserInfo) {
        if ( requestUserInfo == null ) {
            return null;
        }

        UserInfo userInfo = new UserInfo();

        userInfo.setFirstName( requestUserInfo.getFirstName() );
        userInfo.setSecondName( requestUserInfo.getSecondName() );
        userInfo.setBirthdate( requestUserInfo.getBirthdate() );
        userInfo.setBiography( requestUserInfo.getBiography() );
        userInfo.setCity( requestUserInfo.getCity() );

        return userInfo;
    }
}
