package com.example.projecttracker.services;

import com.example.projecttracker.data.ProjectDatahandler;
import com.example.projecttracker.data.UserDataHandler;
import com.example.projecttracker.model.*;
import com.example.projecttracker.util.ToJson;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.UUID;

/**
 * This class is used to handle the requests for the project class.
 *
 * @author Alyssa Heimlicher
 * @version 1.0
 * @since 2022-05-23
 */
@Path("/project")
public class ProjectResource {

    /**
     * This method is used to get all the projects from the json file.
     *
     * @return an arraylist of projects
     * @author Alyssa Heimlicher
     */
    @GET
    @Produces("application/json")
    @Path("/list")
    public Response getAllProjects() {
        try {
            ArrayList<Project> projects = new ProjectDatahandler().getArrayListOutOfJSON();

            return Response.status(200).entity(ToJson.toJson(projects, getFilterProvider())).build();
        } catch (IOException | NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
            return Response.status(500).entity("{\"error\":\"" + e.getMessage() + "\"}").build();
        }
    }

    /**
     * This method is used to get a specific project from the json file based on the uuid.
     *
     * @param uuid the uuid of the project
     * @return a project with the uuid
     * @author Alyssa Heimlicher
     */
    @GET
    @Produces("application/json")
    @Path("/{uuid}")
    public Response getSingleProjectByID(@PathParam("uuid") String uuid) {
        try {
            Project project = new ProjectDatahandler().getSingleFromJsonArray(uuid);

            if (project == null) {
                return Response.status(404).entity("{\"error\":\"Project not found\"}").build();
            }
            return Response.status(200).entity(ToJson.toJson(project, getFilterProvider())).build();
        } catch (IOException | NoSuchFieldException | IllegalAccessException e) {
            return Response.status(500).entity("{\"error\":\"" + e.getMessage() + "\"}").build();
        }
    }

    /**
     * This method is used to add a project to the json file.
     *
     * @param project the project to be added
     * @return a response
     * @author Alyssa Heimlicher
     */
    @POST
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/create")
    public Response createProject(@Valid @BeanParam Project project) {

        try {
            LocalDate startDateLocal = LocalDate.parse(project.getTempStartDate());
            project.setStartDate(startDateLocal);
            User user = new UserDataHandler().readUserByUserUUID(project.getUserid());
            if (user == null) {
                return Response.status(404).entity("{\"error\":\"User not found\"}").build();
            }
            project.setUser(user);
            new ProjectDatahandler().insertIntoJson(project, "projectJSON");
        } catch (IOException | NoSuchFieldException | IllegalAccessException e) {
            return Response.status(500).entity("{\"error\":\"" + e.getMessage() + "\"}").build();
        }

        return Response
                .status(200)
                .entity("")
                .build();
    }

    /**
     * This method deletes a project from the json file by its uuid.
     *
     * @param uuid the uuid of the project
     * @return a response with the status code
     * @author Alyssa Heimlicher
     */
    @DELETE
    @Produces("application/json")
    @Path("/delete/{uuid}")
    public Response deleteProjectByUUID(@PathParam("uuid") String uuid) {
        try {
            Project project = new ProjectDatahandler().getSingleFromJsonArray(uuid);

            if (project == null) {
                return projectNotFound();
            }
        } catch (IOException | NoSuchFieldException | IllegalAccessException e) {
            return projectNotFound();
        }

        try {
            new ProjectDatahandler().deleteSingleFromJson(uuid);
            return Response.status(200).entity("{\"success\":\"Project deleted\"}").build();
        } catch (IOException | NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
            return Response.status(500).entity("{\"error\":\"" + e.getMessage() + "\"}").build();
        } catch (IllegalArgumentException e) {
            return projectNotFound();
        }
    }

    private Response projectNotFound() {
        return Response.status(404).entity("{\"error\":\"Project not found\"}").build();
    }

    /**
     * This method returns the filter provider for the project with every filter in it
     *
     * @return the filter provider
     * @author Alyssa Heimlicher
     */
    private FilterProvider getFilterProvider() {
        return new SimpleFilterProvider()
                .addFilter("ProjectFilter", SimpleBeanPropertyFilter.serializeAllExcept("patchNoteUUIDs", "taskUUIDs", "issueUUIDs", "userUUID"))
                .addFilter("UserFilter", SimpleBeanPropertyFilter.serializeAll());
    }
}
