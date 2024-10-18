package com.garlicbread.includify.service.auth;

import com.garlicbread.includify.util.Constants;
import com.garlicbread.includify.util.Profile;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

@Service
public class TokenService {

  private final JwtEncoder jwtEncoder;

  public TokenService(JwtEncoder jwtEncoder) {
    this.jwtEncoder = jwtEncoder;
  }

  public String generateToken(String username, Profile profile, List<String> authorities) {
    Instant now = Instant.now();

    JwtClaimsSet claims = JwtClaimsSet.builder().issuer(Constants.JWT_ISSUER).issuedAt(now)
        .expiresAt(now.plus(1, ChronoUnit.HOURS)).subject(username)
        .claim(Constants.CLAIM_PROFILE, profile).claim(Constants.CLAIM_AUTHORITY, authorities)
        .build();

    return this.jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
  }
}
