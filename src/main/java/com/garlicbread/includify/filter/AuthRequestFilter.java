package com.garlicbread.includify.filter;

import com.garlicbread.includify.service.auth.ProfileServiceSelector;
import com.garlicbread.includify.util.Constants;
import com.garlicbread.includify.util.Profile;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * Filter to authenticate requests based on JWT tokens.
 * This filter is executed once per request to ensure that the
 * authentication context is set up properly for each incoming request.
 */
@Component
public class AuthRequestFilter extends OncePerRequestFilter {

  private final JwtDecoder jwtDecoder;

  private final ProfileServiceSelector profileServiceSelector;

  public AuthRequestFilter(JwtDecoder jwtDecoder, ProfileServiceSelector profileServiceSelector) {
    this.jwtDecoder = jwtDecoder;
    this.profileServiceSelector = profileServiceSelector;
  }

  @Override
  protected void doFilterInternal(HttpServletRequest request, @NonNull HttpServletResponse response,
                                  @NonNull FilterChain chain) throws ServletException, IOException {
    final String authorizationHeader = request.getHeader(Constants.AUTH_HEADER);

    if (authorizationHeader != null && authorizationHeader.startsWith(
        Constants.BEARER_TOKEN_PREFIX)) {
      String jwtToken = authorizationHeader.substring(7);
      try {
        Jwt jwt = jwtDecoder.decode(jwtToken);
        String username = jwt.getClaimAsString(Constants.SUBJECT);
        String profile = jwt.getClaimAsString(Constants.CLAIM_PROFILE);

        if (username != null && profile != null) {
          UserDetailsService userDetailsService =
              profileServiceSelector.selectService(Profile.valueOf(profile));
          UserDetails userDetails = userDetailsService.loadUserByUsername(username);
          UsernamePasswordAuthenticationToken authentication =
              new UsernamePasswordAuthenticationToken(userDetails, null,
                  userDetails.getAuthorities());
          authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
          SecurityContextHolder.getContext().setAuthentication(authentication);
        } else {
          response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid JWT token");
          return;
        }
      } catch (Exception e) {
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid JWT token");
        return;
      }
    }
    chain.doFilter(request, response);
  }
}
