package com.example.projecttracker.authentication;

import com.example.projecttracker.Config;
import com.example.projecttracker.data.UserDataHandler;
import com.example.projecttracker.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.ws.rs.core.Cookie;
import jakarta.ws.rs.container.ContainerRequestContext;
import lombok.NonNull;

import java.io.IOException;
import java.util.UUID;

public class TokenHandler {
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

    public static Claims decodeToken(String jwt) {
        byte[] apiSecretBytes = Config.getProperty("jwt.secret").getBytes();

        return Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(apiSecretBytes))
                .build()
                .parseClaimsJws(jwt).getBody();
    }

    public static String getUsername(String jwt) {
        return decodeToken(jwt).get("username", String.class);
    }

    public static String getRole(String jwt) {
        return decodeToken(jwt).get("role", String.class);
    }

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

    public static User getUserFromCookie(@NonNull ContainerRequestContext requestContext) throws NotLoggedInException, IOException {
        Cookie jwtCookie = requestContext.getCookies().get(Config.getProperty("jwt.name"));
        return getUserFromJWT(jwtCookie);
    }
}
