package com.example.projecttracker.services;

import com.example.projecttracker.data.ProjectDatahandler;
import com.example.projecttracker.data.UserDataHandler;
import com.example.projecttracker.model.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
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
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());
            objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
            return Response.status(200).entity(objectMapper.writeValueAsString(projects)).build();
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

            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());
            objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
            return Response.status(200).entity(objectMapper.writeValueAsString(project)).build();
        } catch (IOException | NoSuchFieldException | IllegalAccessException e) {
            return Response.status(500).entity("{\"error\":\"" + e.getMessage() + "\"}").build();
        }
    }

    /**
     * This method is used to add a project to the json file.
     *
     * @param title       the title of the project
     * @param description the description of the project
     * @param startDate   the start date of the project
     * @param subject     the subject of the project (e.g. Blender, Game Development, etc.)
     * @param userUUID    the uuid of the user
     * @return a response
     * @author Alyssa Heimlicher
     */
    @POST
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/create")
    public Response createProject(@FormParam("title") String title,
                                  @FormParam("description") String description,
                                  @FormParam("startDate") String startDate,
                                  @FormParam("subject") String subject,
                                  @FormParam("userUUID") String userUUID) {

        try {
            String projectUUID = UUID.randomUUID().toString();
            LocalDate startDateLocal = LocalDate.parse(startDate);
            ArrayList<Task> tasks = new ArrayList<>();
            ArrayList<Issue> issues = new ArrayList<>();
            ArrayList<PatchNote> patchNotes = new ArrayList<>();
            User user = new UserDataHandler().readUserByUserUUID(userUUID);
            if (user == null) {
                return Response.status(404).entity("{\"error\":\"User not found\"}").build();
            }
            Project project = new Project(projectUUID, title, description, startDateLocal, false, subject, user, issues, tasks, patchNotes);
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
            new ProjectDatahandler().deleteSingleFromJson("projectJSON", "projectUUID", uuid);
            return Response.status(200).entity("{\"success\":\"Project deleted\"}").build();
        } catch (IOException | NoSuchFieldException | IllegalAccessException e) {
            return Response.status(500).entity("{\"error\":\"" + e.getMessage() + "\"}").build();
        } catch (IllegalArgumentException e) {
            return Response.status(404).entity("{\"error\":\"Project not found\"}").build();
        }
    }
}
