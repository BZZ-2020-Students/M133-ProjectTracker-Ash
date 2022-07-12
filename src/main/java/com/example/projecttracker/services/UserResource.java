package com.example.projecttracker.services;

import com.example.projecttracker.Config;
import com.example.projecttracker.authentication.NotLoggedInException;
import com.example.projecttracker.authentication.TokenHandler;
import com.example.projecttracker.data.DataHandlerGen;
import com.example.projecttracker.data.ProjectDatahandler;
import com.example.projecttracker.data.UserDataHandler;
import com.example.projecttracker.model.Project;
import com.example.projecttracker.model.User;
import com.example.projecttracker.util.ToJson;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.NewCookie;
import jakarta.ws.rs.core.Response;

import java.io.IOException;
import java.util.ArrayList;

/**
 * This class is used to handle all requests to the user class.
 *
 * @author Alyssa Heimlicher
 * @version 1.0
 * @since 2022-05-23
 */
@Path("/user")
public class UserResource {

    /**
     * This method is used to get all users from the json file.
     *
     * @return an arraylist of all users
     * @author Alyssa Heimlicher
     */
    @GET
    @Produces("application/json")
    @Path("/list")
    public Response getAllUsers() {
        try {
            ArrayList<User> users = new DataHandlerGen<>(User.class).getArrayListOutOfJSON("userJSON");

            return Response.status(200).entity(ToJson.toJson(users, getFilterProvider())).build();
        } catch (IOException e) {
            e.printStackTrace();
            return Response.status(500).entity("{\"error\":\"" + e.getMessage() + "\"}").build();
        }
    }

    /**
     * This method is used to get a user from the json file by their uuid.
     *
     * @param uuid the uuid of the user
     * @return the user with the given uuid
     * @author Alyssa Heimlicher
     */
    @GET
    @Produces("application/json")
    @Path("/{uuid}")
    public Response getSingleUserByUUID(@PathParam("uuid") String uuid) {
        try {
            User user = new DataHandlerGen<>(User.class).getSingleFromJsonArray("userJSON", "userUUID", uuid);
            if (user == null) {
                return Response.status(404).entity("{\"error\":\"User not found\"}").build();
            }

            return Response.status(200).entity(ToJson.toJson(user, getFilterProvider())).build();
        } catch (IOException | NoSuchFieldException | IllegalAccessException e) {
            return Response.status(500).entity("{\"error\":\"" + e.getMessage() + "\"}").build();
        }
    }

    /**
     * Creates a new user and adds it to the json file.
     *
     * @param user           the user to be added
     * @param requestContext contains the token of the user
     * @return a response depending on the success of the operation.
     * @author Alyssa Heimlicher
     */
    @RolesAllowed({"admin"})
    @POST
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/create")
    public Response createPatchNote(@Valid @BeanParam User user, ContainerRequestContext requestContext) {
        User userFromCookie;
        try {
            userFromCookie = TokenHandler.getUserFromCookie(requestContext);
        } catch (NotLoggedInException | IOException e) {
            return Response.status(Response.Status.UNAUTHORIZED).entity("{\"error\":\"" + e.getMessage() + "\"}").build();
        }
        if (userFromCookie.getUserRole().equalsIgnoreCase("admin")) {
            new UserDataHandler().insertIntoJson(user, "userJSON");

            return Response
                    .status(200)
                    .entity("")
                    .build();

        }
        return Response.status(Response.Status.UNAUTHORIZED).entity("{\"error\":\"You are not authorized to create a user. Stop trying!\"}").build();
    }

    /**
     * This method deletes a user from the json file by their uuid.
     * Deletes the users projects as well.
     *
     * @param uuid           the uuid of the user
     * @param requestContext contains the token of the user
     * @return a response with the status code
     * @author Alyssa Heimlicher
     */
    @RolesAllowed({"admin"})
    @DELETE
    @Produces("application/json")
    @Path("/delete/{uuid}")
    public Response deleteUserByUUID(@PathParam("uuid") String uuid, ContainerRequestContext requestContext) {
        User userFromCookie;
        try {
            userFromCookie = TokenHandler.getUserFromCookie(requestContext);
        } catch (NotLoggedInException | IOException e) {
            return Response.status(Response.Status.UNAUTHORIZED).entity("{\"error\":\"" + e.getMessage() + "\"}").build();
        }
        if (userFromCookie.getUserRole().equalsIgnoreCase("admin")) {
            try {
                ArrayList<Project> projects = new ProjectDatahandler().getArrayListOutOfJSONByUserUUID(uuid);
                for (Project project : projects) {
                    new ProjectDatahandler().deleteSingleFromJson(project.getProjectUUID());
                }
                new UserDataHandler().deleteSingleFromJson("userJSON", "userUUID", uuid);
                return Response.status(200).entity("{\"success\":\"User deleted\"}").build();
            } catch (IOException | NoSuchFieldException | IllegalAccessException e) {
                return Response.status(500).entity("{\"error\":\"" + e.getMessage() + "\"}").build();
            } catch (IllegalArgumentException e) {
                return Response.status(404).entity("{\"error\":\"User not found\"}").build();
            }
        }
        return Response.status(Response.Status.UNAUTHORIZED).entity("{\"error\":\"You are not authorized to delete a user. Dont be rude!\"}").build();
    }

    /**
     * This method updates a user in the json file by their uuid.
     *
     * @param uuid           the uuid of the user
     * @param user           the user to be updated
     * @param requestContext contains the token of the user
     * @return a response depending on the success of the operation.
     * @throws IOException            if the json file cannot be found
     * @throws NoSuchFieldException   if the field cannot be found
     * @throws IllegalAccessException if the file cannot be accessed
     * @author Alyssa Heimlicher
     */
    @RolesAllowed({"admin"})
    @PUT
    @Produces("application/json")
    @Path("/update/{uuid}")
    public Response updateUser(@PathParam("uuid") String uuid, @Valid @BeanParam User user, ContainerRequestContext requestContext) throws IOException, NoSuchFieldException, IllegalAccessException {
        User userFromCookie;
        try {
            userFromCookie = TokenHandler.getUserFromCookie(requestContext);
        } catch (NotLoggedInException | IOException e) {
            return Response.status(Response.Status.UNAUTHORIZED).entity("{\"error\":\"" + e.getMessage() + "\"}").build();
        }
        boolean changed = false;
        User toBeUpdatedUser = new UserDataHandler().readUserByUserUUID(uuid);

        if (toBeUpdatedUser == null) {
            return Response.status(404).entity("{\"error\":\"User not found\"}").build();
        }

        if (user.getUserName() != null && !user.getUserName().equals(toBeUpdatedUser.getUserName())) {
            toBeUpdatedUser.setUserName(user.getUserName());
            changed = true;
        }

        if (user.getPassword() != null && !user.getPassword().equals(toBeUpdatedUser.getPassword())) {
            toBeUpdatedUser.setPassword(user.getPassword());
            changed = true;
        }
        if (userFromCookie.getUserRole().equalsIgnoreCase("admin")) {
            if (changed) {
                new UserDataHandler().updateSingleFromJson("userJSON", "userUUID", uuid, toBeUpdatedUser);
                return Response.status(200).entity("{\"success\":\"User updated\"}").build();
            }

            return Response.status(200).entity("{\"success\":\"No changes made\"}").build();
        }
        return Response.status(Response.Status.UNAUTHORIZED).entity("{\"error\":\"You are not authorized to update a user." +
                " You're wasting your energy!\"}").build();
    }


    /**
     * This method is used for the login process
     *
     * @param username the username of the user
     * @param password the password of the user
     * @return a response depending on the success of the operation.
     *
     * @author Alyssa Heimlicher
     */
    @POST
    @Produces("application/json")
    @Path("/login")
    public Response login(@FormParam("username") String username,
                          @FormParam("password") String password) {
        try {
            User userFromJson = new UserDataHandler().readUserByUsername(username);
            if (userFromJson == null) {
                return Response.status(404).entity("{\"error\":\"User not found\"}").build();
            }
            if (!userFromJson.getPassword().equals(password)) {
                return Response.status(401).entity("{\"error\":\"Wrong password\"}").build();
            }

            NewCookie tokenCookie = new NewCookie(
                    Config.getProperty("jwt.name"),
                    TokenHandler.createToken(userFromJson),
                    "/",
                    "",
                    "Auth-Token",
                    86400,
                    false);

            return Response
                    .status(200)
                    .header("Access-Control-Allow-Origin", "*")
                    .header("Access-Control-Allow-Credentials", "true")
                    .header("Access-Control-Allow-Headers",
                            "origin, content-type, accept, authorization")
                    .header("Access-Control-Allow-Methods",
                            "GET, POST, DELETE")
                    .entity("")
                    .cookie(tokenCookie)
                    .build();


        } catch (IOException e) {
            return Response.status(500).entity("{\"error\":\"" + e.getMessage() + "\"}").build();
        }
    }

    /**
     * This method is used for the logout process
     * @return a response depending on the success of the operation.
     *
     * @author Alyssa Heimlicher
     */
    @POST
    @Produces("application/json")
    @Path("/logout")
    public Response logout() {
        NewCookie tokenCookie = new NewCookie(
                Config.getProperty("jwt.name"),
                "",
                "/",
                "",
                "Auth-Token",
                0,
                false
        );

        return Response
                .status(200)
                .header("Access-Control-Allow-Origin", "*")
                .header("Access-Control-Allow-Credentials", "true")
                .header("Access-Control-Allow-Headers",
                        "origin, content-type, accept, authorization")
                .header("Access-Control-Allow-Methods",
                        "GET, POST, DELETE")
                .entity("")
                .cookie(tokenCookie)
                .build();
    }


    /**
     * This method returns a filter provider for the user class.
     *
     * @return a filter provider for the user class
     * @author Alyssa Heimlicher
     */
    private FilterProvider getFilterProvider() {
        return new SimpleFilterProvider()
                .addFilter("UserFilter", SimpleBeanPropertyFilter.serializeAll());
    }
}
