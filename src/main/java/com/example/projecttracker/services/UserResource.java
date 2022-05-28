package com.example.projecttracker.services;

import com.example.projecttracker.data.DataHandlerGen;
import com.example.projecttracker.data.UserDataHandler;
import com.example.projecttracker.model.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

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
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.findAndRegisterModules();
            return Response.status(200).entity(objectMapper.writeValueAsString(users)).build();
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
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());
            objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
            if (user == null) {
                return Response.status(404).entity("{\"error\":\"User not found\"}").build();
            }

            return Response.status(200).entity(objectMapper.writeValueAsString(user)).build();
        } catch (IOException | NoSuchFieldException | IllegalAccessException e) {
            return Response.status(500).entity("{\"error\":\"" + e.getMessage() + "\"}").build();
        }
    }

    /**
     * Creates a new user and adds it to the json file.
     *
     * @param username the username of the user
     * @param password the password of the user
     * @return a response depending on the success of the operation.
     * @author Alyssa Heimlicher
     */
    @POST
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/create")
    public Response createPatchNote(@FormParam("username") String username,
                                    @FormParam("password") String password) {
        String userUUID = UUID.randomUUID().toString();
        User user = new User(userUUID, username, password, "Guest");
        new UserDataHandler().insertIntoJson(user, "userJSON");

        return Response
                .status(200)
                .entity("")
                .build();
    }
}
