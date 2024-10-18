package com.garlicbread.includify.model.auth;

/**
 * Represents the response object for authentication.
 * This class holds the JSON Web Token (JWT) generated upon successful authentication.
 *
 * @param jwt the JSON Web Token as a string
 */
public record AuthResponse(String jwt) {
}