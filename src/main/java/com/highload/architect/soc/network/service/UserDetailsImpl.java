package com.highload.architect.soc.network.service;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.highload.architect.soc.network.model.AccountInfo;
import com.highload.architect.soc.network.model.UserInfo;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serial;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class UserDetailsImpl implements UserDetails {
    @Serial
    private static final long serialVersionUID = 1L;
    private final UUID id;
    @JsonIgnore
    private final String password;
    private final Collection<? extends GrantedAuthority> authorities;

    public UserDetailsImpl(final UserInfo userInfo, AccountInfo accountInfo, final Collection<? extends GrantedAuthority> authorities) {
        this.id = userInfo.getId();
        this.password = accountInfo.getPassword();
        this.authorities = authorities;
    }


    public static UserDetailsImpl build(UserInfo userInfo, AccountInfo accountInfo) {
        return new UserDetailsImpl(userInfo, accountInfo, buildGrantedAuthorities());
    }

    private static List<GrantedAuthority> buildGrantedAuthorities() {
        return List.of(new SimpleGrantedAuthority("user"));
    }

    public UUID getId() {
        return id;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return getId().toString();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        UserDetailsImpl user = (UserDetailsImpl) o;
        return Objects.equals(id, user.id);
    }
}
