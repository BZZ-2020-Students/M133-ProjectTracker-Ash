package com.example.projecttracker.services;

import com.example.projecttracker.data.DataHandlerGen;
import com.example.projecttracker.data.ProjectDatahandler;
import com.example.projecttracker.data.UserDataHandler;
import com.example.projecttracker.model.Project;
import com.example.projecttracker.model.User;
import com.example.projecttracker.util.ToJson;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import jakarta.validation.Valid;
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
     * @param user the user to be added
     * @return a response depending on the success of the operation.
     * @author Alyssa Heimlicher
     */
    @POST
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/create")
    public Response createPatchNote(@Valid @BeanParam User user) {
        new UserDataHandler().insertIntoJson(user, "userJSON");

        return Response
                .status(200)
                .entity("")
                .build();
    }

    /**
     * This method deletes a user from the json file by their uuid.
     * Deletes the users projects as well.
     *
     * @param uuid the uuid of the user
     * @return a response with the status code
     * @author Alyssa Heimlicher
     */
    @DELETE
    @Produces("application/json")
    @Path("/delete/{uuid}")
    public Response deleteUserByUUID(@PathParam("uuid") String uuid) {
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

    /**
     * This method returns a filter provider for the user class.
     *
     * @return a filter provider for the user class
     */
    private FilterProvider getFilterProvider() {
        return new SimpleFilterProvider()
                .addFilter("UserFilter", SimpleBeanPropertyFilter.serializeAll());
    }
}
