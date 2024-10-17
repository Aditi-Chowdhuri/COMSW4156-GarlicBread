package com.garlicbread.includify.profile.user;

import com.garlicbread.includify.entity.user.User;
import com.garlicbread.includify.util.Constants;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.List;

public class UserDetails implements org.springframework.security.core.userdetails.UserDetails {

    private final User user;

    public UserDetails(User user) {
        this.user = user;
    }

    @Override
    public String getUsername() {
        return user.getEmail();
    }

    @Override
    public String getPassword() {
        return user.getHashedPassword();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of((GrantedAuthority) () -> Constants.AUTHORITY_USER);
    }
}
