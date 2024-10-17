package com.garlicbread.includify.model.auth;

import com.garlicbread.includify.util.Profile;

public class AuthRequest {

    private String email;
    private String password;
    private String profile;

    public String getEmail() {
        return email;
    }

    public Profile getProfile() { return Profile.valueOf(profile.toUpperCase()); }

    public String getPassword() {
        return password;
    }
}