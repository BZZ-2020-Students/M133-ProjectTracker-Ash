package com.example.projecttracker.services;

import com.example.projecttracker.authentication.NotLoggedInException;
import com.example.projecttracker.authentication.TokenHandler;
import com.example.projecttracker.data.ProjectDatahandler;
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
import jakarta.ws.rs.core.Response;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Objects;

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
     * @param project        the project to be added
     * @param requestContext the context of the token
     * @return a response
     * @author Alyssa Heimlicher
     */
    @RolesAllowed({"admin", "user"})
    @POST
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/create")
    public Response createProject(@Valid @BeanParam Project project, ContainerRequestContext requestContext) {
        User user;
        try {
            user = TokenHandler.getUserFromCookie(requestContext);
        } catch (NotLoggedInException e) {
            return Response.status(Response.Status.UNAUTHORIZED).entity("{\"error\":\"" + e.getMessage() + "\"}").build();
        } catch (IOException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("{\"error\":\"" + e.getMessage() + "\"}").build();
        }


        LocalDate startDateLocal = LocalDate.parse(project.getTempStartDate());
        project.setStartDate(startDateLocal);
        project.setUser(user);
        new ProjectDatahandler().insertIntoJson(project, "projectJSON");


        return Response
                .status(200)
                .entity("")
                .build();
    }

    /**
     * This method deletes a project from the json file by its uuid.
     *
     * @param uuid           the uuid of the project
     * @param requestContext the context of the token
     * @return a response with the status code
     * @author Alyssa Heimlicher
     */
    @RolesAllowed({"admin", "user"})
    @DELETE
    @Produces("application/json")
    @Path("/delete/{uuid}")
    public Response deleteProjectByUUID(@PathParam("uuid") String uuid, ContainerRequestContext requestContext) {
        User user;
        try {
            user = TokenHandler.getUserFromCookie(requestContext);
        } catch (NotLoggedInException e) {
            return Response.status(Response.Status.UNAUTHORIZED).entity("{\"error\":\"" + e.getMessage() + "\"}").build();
        } catch (IOException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("{\"error\":\"" + e.getMessage() + "\"}").build();
        }
        Project project;
        try {
            project = new ProjectDatahandler().getSingleFromJsonArray(uuid);

            if (project == null) {
                return projectNotFound();
            }


        } catch (IOException | NoSuchFieldException | IllegalAccessException e) {
            return projectNotFound();
        }

        if ("admin".equalsIgnoreCase(user.getUserRole()) || project.getUser().getUserUUID().equals(user.getUserUUID())) {
            try {
                new ProjectDatahandler().deleteSingleFromJson(uuid);
                return Response.status(200).entity("{\"success\":\"Project deleted\"}").build();
            } catch (IOException | NoSuchFieldException | IllegalAccessException e) {
                e.printStackTrace();
                return Response.status(500).entity("{\"error\":\"" + e.getMessage() + "\"}").build();
            } catch (IllegalArgumentException e) {
                return projectNotFound();
            }
        } else {
            return Response.status(Response.Status.UNAUTHORIZED).entity("{\"error\":\"You are not allowed to delete this project\"}").build();
        }

    }

    /**
     * this method is to return a 404 error if the project is not found.
     *
     * @return a response with the status code 404
     */
    private Response projectNotFound() {
        return Response.status(404).entity("{\"error\":\"Project not found\"}").build();
    }

    /**
     * This method is used to update a project in the json file.
     *
     * @param uuid           the uuid of the project
     * @param project        the project to be updated
     * @param requestContext the context of the token
     * @return a response with the status code
     * @throws IOException            if the json file is not found
     * @throws NoSuchFieldException   if the field is not found
     * @throws IllegalAccessException if the field is not accessible
     * @author Alyssa Heimlicher
     */
    @RolesAllowed({"admin", "user"})
    @PUT
    @Produces("application/json")
    @Path("/update/{uuid}")
    public Response updateProject(@PathParam("uuid") String uuid, @Valid @BeanParam Project project, ContainerRequestContext requestContext) throws IOException, NoSuchFieldException, IllegalAccessException {
        User user;
        try {
            user = TokenHandler.getUserFromCookie(requestContext);
        } catch (NotLoggedInException | IOException e) {
            return Response.status(Response.Status.UNAUTHORIZED).entity("{\"error\":\"" + e.getMessage() + "\"}").build();
        }
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

        if ("admin".equalsIgnoreCase(user.getUserRole()) || Objects.equals(user.getUserUUID(), toBeUpdatedProject.getUser().getUserUUID())) {
            if (changed) {
                new ProjectDatahandler().updateSingleFromJson("projectJSON", "projectUUID", uuid, toBeUpdatedProject);
                return Response.status(200).entity("{\"success\":\"Project updated\"}").build();
            }
            return Response.status(200).entity("{\"success\":\"No changes made\"}").build();
        }

        return Response.status(Response.Status.UNAUTHORIZED).entity("{\"error\":\"You are not allowed to update this project\"}").build();

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
