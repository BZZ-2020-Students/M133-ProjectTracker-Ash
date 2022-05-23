package com.example.projecttracker.services;

import com.example.projecttracker.data.DataHandlerGen;
import com.example.projecttracker.model.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
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
            ObjectMapper objectMapper = new ObjectMapper();
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

            if (user == null) {
                return Response.status(404).entity("{\"error\":\"User not found\"}").build();
            }

            return Response.status(200).entity(objectMapper.writeValueAsString(user)).build();
        } catch (IOException | NoSuchFieldException | IllegalAccessException e) {
            return Response.status(500).entity("{\"error\":\"" + e.getMessage() + "\"}").build();
        }
    }
}
