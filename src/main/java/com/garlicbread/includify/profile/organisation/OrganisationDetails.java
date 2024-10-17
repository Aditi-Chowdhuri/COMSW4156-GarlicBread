package com.garlicbread.includify.profile.organisation;

import com.garlicbread.includify.entity.organisation.Organisation;
import com.garlicbread.includify.util.Constants;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public class OrganisationDetails implements UserDetails {

    private final Organisation organisation;

    public OrganisationDetails(Organisation organisation) {
        this.organisation = organisation;
    }

    @Override
    public String getUsername() {
        return organisation.getEmail();
    }

    @Override
    public String getPassword() {
        return organisation.getPassword();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of((GrantedAuthority) () -> Constants.AUTHORITY_ORGANISATION);
    }
}
