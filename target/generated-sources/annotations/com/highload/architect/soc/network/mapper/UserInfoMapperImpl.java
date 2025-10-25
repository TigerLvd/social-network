package com.highload.architect.soc.network.mapper;

import com.highload.architect.soc.network.model.User;
import com.highload.architect.soc.network.model.UserInfo;
import com.highload.architect.soc.network.model.UserRegisterPostRequest;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-10-25T11:02:17+0500",
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

        user.setBiography( userInfo.getBiography() );
        user.setBirthdate( userInfo.getBirthdate() );
        user.setCity( userInfo.getCity() );
        user.setFirstName( userInfo.getFirstName() );
        if ( userInfo.getId() != null ) {
            user.setId( userInfo.getId().toString() );
        }
        user.setSecondName( userInfo.getSecondName() );

        return user;
    }

    @Override
    public UserInfo toEntity(UserRegisterPostRequest requestUserInfo) {
        if ( requestUserInfo == null ) {
            return null;
        }

        UserInfo userInfo = new UserInfo();

        userInfo.setBiography( requestUserInfo.getBiography() );
        userInfo.setBirthdate( requestUserInfo.getBirthdate() );
        userInfo.setCity( requestUserInfo.getCity() );
        userInfo.setFirstName( requestUserInfo.getFirstName() );
        userInfo.setSecondName( requestUserInfo.getSecondName() );

        return userInfo;
    }
}
