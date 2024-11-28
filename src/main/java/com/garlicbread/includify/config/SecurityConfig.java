package com.garlicbread.includify.config;

import com.garlicbread.includify.service.auth.OrganisationDetailsService;
import com.garlicbread.includify.service.auth.UserDetailsService;
import com.garlicbread.includify.service.auth.VolunteerDetailsService;
import com.garlicbread.includify.util.Constants;
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.UUID;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Security configuration class for the application.
 * <p>
 * This class configures the security settings for the application,
 * including authentication managers for different user types,
 * JWT encoding/decoding, and HTTP security configurations.
 * </p>
 */
@Configuration
public class SecurityConfig {

  private final OrganisationDetailsService organisationDetailsService;
  private final VolunteerDetailsService volunteerDetailsService;
  private final UserDetailsService userDetailsService;

  /**
   * Constructs a SecurityConfig with the specified details services.
   *
   * @param organisationDetailsService the service for organization details
   * @param volunteerDetailsService    the service for volunteer details
   * @param userDetailsService         the service for user details
   */
  public SecurityConfig(OrganisationDetailsService organisationDetailsService,
                        VolunteerDetailsService volunteerDetailsService,
                        UserDetailsService userDetailsService) {
    this.organisationDetailsService = organisationDetailsService;
    this.volunteerDetailsService = volunteerDetailsService;
    this.userDetailsService = userDetailsService;
  }

  /**
   * Provides a KeyPair for RSA encryption.
   *
   * @return a KeyPair instance
   * @throws Exception if an error occurs while generating the key pair
   */
  @Bean
  public KeyPair keyPair() throws Exception {
    KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
    keyPairGenerator.initialize(2048);
    return keyPairGenerator.generateKeyPair();
  }

  /**
   * Provides a JWKSource for JWT signing and verification.
   *
   * @param keyPair the RSA key pair used for signing
   * @return a JWKSource instance
   */
  @Bean
  public JWKSource<SecurityContext> jwkSource(KeyPair keyPair) {
    RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
    RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();

    JWK jwk =
        new RSAKey.Builder(publicKey).privateKey(privateKey).keyID(UUID.randomUUID().toString())
            .build();

    JWKSet jwkSet = new JWKSet(jwk);
    return (jwkSelector, securityContext) -> jwkSelector.select(jwkSet);
  }

  @Bean
  public JwtEncoder jwtEncoder(JWKSource<SecurityContext> jwkSource) {
    return new NimbusJwtEncoder(jwkSource);
  }

  @Bean
  public JwtDecoder jwtDecoder(KeyPair keyPair) {
    RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
    return NimbusJwtDecoder.withPublicKey(publicKey).build();
  }

  /**
   * Configures the security filter chain for HTTP requests.
   *
   * @param http the HttpSecurity instance to configure
   * @return a SecurityFilterChain instance
   * @throws Exception if an error occurs while configuring HTTP security
   */
  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http.csrf(AbstractHttpConfigurer::disable).authorizeHttpRequests(
        authorizeRequests -> authorizeRequests.requestMatchers(Constants.NO_AUTH_ENDPOINTS)
            .permitAll().anyRequest().authenticated()).oauth2ResourceServer(
                    oauth2 -> oauth2.jwt(jwt -> jwt.jwtAuthenticationConverter(
                            jwtAuthenticationConverter())));

    return http.build();
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  /**
   * Provides an AuthenticationManager for organization authentication.
   *
   * @param http the HttpSecurity instance to configure
   * @return an AuthenticationManager instance
   * @throws Exception if an error occurs while creating the AuthenticationManager
   */
  @Bean(name = Constants.ORGANISATION_AUTHENTICATION_MANAGER)
  @Primary
  public AuthenticationManager organisationAuthenticationManager(HttpSecurity http)
      throws Exception {
    DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
    authenticationProvider.setUserDetailsService(organisationDetailsService);
    authenticationProvider.setPasswordEncoder(passwordEncoder());
    return new ProviderManager(authenticationProvider);
  }

  /**
   * Provides an AuthenticationManager for volunteer authentication.
   *
   * @param http the HttpSecurity instance to configure
   * @return an AuthenticationManager instance
   * @throws Exception if an error occurs while creating the AuthenticationManager
   */
  @Bean(name = Constants.VOLUNTEER_AUTHENTICATION_MANAGER)
  public AuthenticationManager volunteerAuthenticationManager(HttpSecurity http) throws Exception {
    DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
    authenticationProvider.setUserDetailsService(volunteerDetailsService);
    authenticationProvider.setPasswordEncoder(passwordEncoder());
    return new ProviderManager(authenticationProvider);
  }

  /**
   * Provides an AuthenticationManager for user authentication.
   *
   * @param http the HttpSecurity instance to configure
   * @return an AuthenticationManager instance
   * @throws Exception if an error occurs while creating the AuthenticationManager
   */
  @Bean(name = Constants.USER_AUTHENTICATION_MANAGER)
  public AuthenticationManager userAuthenticationManager(HttpSecurity http) throws Exception {
    DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
    authenticationProvider.setUserDetailsService(userDetailsService);
    authenticationProvider.setPasswordEncoder(passwordEncoder());
    return new ProviderManager(authenticationProvider);
  }

  private JwtAuthenticationConverter jwtAuthenticationConverter() {
    return new JwtAuthenticationConverter();
  }
}
