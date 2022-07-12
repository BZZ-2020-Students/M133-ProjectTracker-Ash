package com.example.projecttracker.authentication;

import com.example.projecttracker.Config;
import com.example.projecttracker.data.UserDataHandler;
import com.example.projecttracker.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.core.Cookie;
import lombok.NonNull;

import java.io.IOException;
import java.util.UUID;

/**
 * Creates and decodes JWT tokens.
 *
 * @author Alyssa Heimlicher
 * @since 1.3
 */
public class TokenHandler {
    /**
     * creates a JWT token for the user.
     *
     * @param user the user to create the token for.
     * @return the JWT token.
     * @author Alyssa Heimlicher
     */
    public static String createToken(User user) {

        byte[] apiSecretBytes = Config.getProperty("jwt.secret").getBytes();
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
        JwtBuilder builder = Jwts.builder()
                .setId(UUID.randomUUID().toString())
                .setIssuer(Config.getProperty("jwt.issuer"))
                .signWith(Keys.hmacShaKeyFor(apiSecretBytes), signatureAlgorithm);

        return builder
                .claim("username", user.getUserName())
                .claim("role", user.getUserRole())
                .compact();
    }

    /**
     * Decodes the JWT token.
     *
     * @param jwt the JWT token.
     * @return claims from the JWT token.
     * @author Alyssa Heimlicher
     */
    public static Claims decodeToken(String jwt) {
        byte[] apiSecretBytes = Config.getProperty("jwt.secret").getBytes();

        return Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(apiSecretBytes))
                .build()
                .parseClaimsJws(jwt).getBody();
    }

    /**
     * Gets the username from the JWT token.
     *
     * @param jwt the JWT token.
     * @return the username from the JWT token.
     * @author Alyssa Heimlicher
     */
    public static String getUsername(String jwt) {
        return decodeToken(jwt).get("username", String.class);
    }

    /**
     * Gets the role from the JWT token.
     *
     * @param jwt the JWT token.
     * @return the role from the JWT token.
     * @author Alyssa Heimlicher
     */
    public static String getRole(String jwt) {
        return decodeToken(jwt).get("role", String.class);
    }

    /**
     * Gets the user from the JWT token.
     *
     * @param jwtCookie the JWT cookie.
     * @return the user from the JWT token.
     * @throws NotLoggedInException if the user is not logged in.
     * @throws IOException          if the user cannot be read.
     * @author Alyssa Heimlicher
     */
    public static User getUserFromJWT(Cookie jwtCookie) throws NotLoggedInException, IOException {
        if (jwtCookie == null) {
            throw new NotLoggedInException();
        }

        String jwt = jwtCookie.getValue();
        UserDataHandler userDataHandler = new UserDataHandler();
        String username = getUsername(jwt);
        User user = userDataHandler.readUserByUsername(username);

        if (user == null) {
            throw new IllegalArgumentException("User not found.");
        }

        return user;
    }

    /**
     * Gets the user from the JWT cookie
     *
     * @param requestContext the request context.
     * @return the user from the JWT cookie.
     * @throws NotLoggedInException if the user is not logged in.
     * @throws IOException          if the user cannot be read.
     */
    public static User getUserFromCookie(@NonNull ContainerRequestContext requestContext) throws NotLoggedInException, IOException {
        Cookie jwtCookie = requestContext.getCookies().get(Config.getProperty("jwt.name"));
        return getUserFromJWT(jwtCookie);
    }
}
