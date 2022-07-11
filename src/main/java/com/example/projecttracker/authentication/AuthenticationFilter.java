package com.example.projecttracker.authentication;

import com.example.projecttracker.Config;
import com.example.projecttracker.model.User;
import jakarta.annotation.security.DenyAll;
import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.container.ResourceInfo;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Cookie;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.Provider;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;


@Provider
public class AuthenticationFilter implements ContainerRequestFilter {
    @Context
    ResourceInfo resourceInfo;

    @Override
    public void filter(ContainerRequestContext requestContext) {
        Method method = resourceInfo.getResourceMethod();

        if (method.isAnnotationPresent(DenyAll.class)) {
            requestContext.abortWith(Response.status(Response.Status.FORBIDDEN)
                    .entity("No roles are allowed. Continuing will be counted as trespassing!").build());
        } else if (!method.isAnnotationPresent(PermitAll.class) &&
                method.isAnnotationPresent(RolesAllowed.class)) {
            RolesAllowed rolesAnnotation = method.getAnnotation(RolesAllowed.class);
            Set<String> requiredRoles = new HashSet<>(Arrays.asList(rolesAnnotation.value()));

            User user = null;
            try {
                user = TokenHandler.getUserFromCookie(requestContext);
            } catch (NotLoggedInException e) {
                requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED)
                        .entity(e.getMessage()).build());
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (user == null || !isUserAllowed(requiredRoles, user.getUserRole())) {
                requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED)
                        .entity("You cannot access this resource").build());
            }
        }
    }

    private boolean isUserAllowed(final Set<String> requiredRoles, String userRole) {
        return requiredRoles.stream().anyMatch(role -> role.equalsIgnoreCase(userRole));
    }
}