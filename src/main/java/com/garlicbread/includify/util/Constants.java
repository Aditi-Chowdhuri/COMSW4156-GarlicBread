package com.garlicbread.includify.util;

/**
 * This class contains constant values used throughout our Includify application.
 * It includes authentication-related constants, authority types, claim keys,
 * and endpoints that do not require authentication.
 */
public class Constants {

  public static final String ORGANISATION_AUTHENTICATION_MANAGER =
      "organisationAuthenticationManager";
  public static final String USER_AUTHENTICATION_MANAGER = "userAuthenticationManager";
  public static final String VOLUNTEER_AUTHENTICATION_MANAGER = "volunteerAuthenticationManager";
  public static final String AUTHORITY_ORGANISATION = "ORGANISATION";
  public static final String AUTHORITY_USER = "USER";
  public static final String AUTHORITY_VOLUNTEER = "VOLUNTEER";
  public static final String SUBJECT = "sub";
  public static final String CLAIM_AUTHORITY = "authorities";
  public static final String CLAIM_PROFILE = "profile";
  public static final String AUTH_HEADER = "Authorization";
  public static final String BEARER_TOKEN_PREFIX = "Bearer ";
  public static final String JWT_ISSUER = "http://localhost:8080/";
  public static final String[] NO_AUTH_ENDPOINTS =
      new String[]{"/organisation/login", "/volunteer/login", "/registration/login", "/volunteer"
          + "/add", "/registration/create", "/organisation/create", "/resource/all",
          "/resource/{id}", "/organisation/all", "/organisation/{id}", "/", "/registration"
          + "/deleteCategory/{id}", "/registration/createCategory", "registration/category/all"};
}
