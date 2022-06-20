package com.example.projecttracker.services;

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
import java.time.LocalDate;
import java.util.ArrayList;

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
     * This method is used to update a project in the json file.
     *
     * @param uuid    the uuid of the project
     * @param project the project to be updated
     * @return a response with the status code
     * @throws IOException if the json file is not found
     * @throws NoSuchFieldException if the field is not found
     * @throws IllegalAccessException if the field is not accessible
     *
     * @author Alyssa Heimlicher
     */
    @PUT
    @Produces("application/json")
    @Path("/update/{uuid}")
    public Response updateProject(@PathParam("uuid") String uuid, @Valid @BeanParam Project project) throws IOException, NoSuchFieldException, IllegalAccessException {
        boolean changed = false;
        Project toBeUpdatedProject = new ProjectDatahandler().getSingleFromJsonArray(uuid);
        if (toBeUpdatedProject == null) {
            return projectNotFound();
        }

        if (project.getTitle() != null && !project.getTitle().equals(toBeUpdatedProject.getTitle())) {
            toBeUpdatedProject.setTitle(project.getTitle());
            changed = true;
        }
        if (project.getDescription() != null && !project.getDescription().equals(toBeUpdatedProject.getDescription())) {
            toBeUpdatedProject.setDescription(project.getDescription());
            changed = true;
        }
        if (project.getStartDate() != null && !project.getStartDate().equals(toBeUpdatedProject.getStartDate())) {
            toBeUpdatedProject.setStartDate(project.getStartDate());
            changed = true;
        }
        if (project.getSubject() != null && !project.getSubject().equals(toBeUpdatedProject.getSubject())) {
            toBeUpdatedProject.setSubject(project.getSubject());
            changed = true;
        }

        if (changed) {
            new ProjectDatahandler().updateSingleFromJson("projectJSON", "projectUUID", uuid, toBeUpdatedProject);
            return Response.status(200).entity("{\"success\":\"Project updated\"}").build();
        }
        return Response.status(200).entity("{\"success\":\"No changes made\"}").build();

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
