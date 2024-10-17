package com.garlicbread.includify.controller.auth;

import com.garlicbread.includify.model.auth.AuthRequest;
import com.garlicbread.includify.service.auth.TokenService;
import com.garlicbread.includify.util.Constants;
import jakarta.annotation.security.PermitAll;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class AuthController {

    private final TokenService tokenService;

    private final AuthenticationManager organisationAuthenticationManager;
    private final AuthenticationManager userAuthenticationManager;
    private final AuthenticationManager volunteerAuthenticationManager;

    public AuthController(
        TokenService tokenService,
        @Qualifier(Constants.ORGANISATION_AUTHENTICATION_MANAGER) AuthenticationManager organisationAuthenticationManager,
        @Qualifier(Constants.USER_AUTHENTICATION_MANAGER) AuthenticationManager userAuthenticationManager,
        @Qualifier(Constants.VOLUNTEER_AUTHENTICATION_MANAGER) AuthenticationManager volunteerAuthenticationManager
    ) {
        this.tokenService = tokenService;

        this.organisationAuthenticationManager = organisationAuthenticationManager;
        this.userAuthenticationManager = userAuthenticationManager;
        this.volunteerAuthenticationManager = volunteerAuthenticationManager;
    }

    @PostMapping("/organisation/login")
    @PermitAll
    public String organisationLogin(@RequestBody AuthRequest authRequest) {
        return processLogin(organisationAuthenticationManager, authRequest);
    }

    @PostMapping("/volunteer/login")
    @PermitAll
    public String volunteerLogin(@RequestBody AuthRequest authRequest) {
        return processLogin(volunteerAuthenticationManager, authRequest);
    }

    @PostMapping("/user/login")
    @PermitAll
    public String userLogin(@RequestBody AuthRequest authRequest) {
        return processLogin(userAuthenticationManager, authRequest);
    }

    private String processLogin(AuthenticationManager authenticationManager, AuthRequest authRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authRequest.getEmail(), authRequest.getPassword())
            );

            List<String> authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

            return tokenService.generateToken(
                authRequest.getEmail(),
                authRequest.getProfile(),
                authorities
            );

        } catch (AuthenticationException e) {
            throw new RuntimeException("Invalid username or password");
        }
    }
}
