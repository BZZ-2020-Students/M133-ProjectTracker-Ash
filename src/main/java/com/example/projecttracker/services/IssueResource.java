package com.example.projecttracker.services;

import com.example.projecttracker.authentication.NotLoggedInException;
import com.example.projecttracker.authentication.TokenHandler;
import com.example.projecttracker.data.DataHandlerGen;
import com.example.projecttracker.data.IssueDataHandler;
import com.example.projecttracker.data.ProjectDatahandler;
import com.example.projecttracker.model.Issue;
import com.example.projecttracker.model.Project;
import com.example.projecttracker.model.Task;
import com.example.projecttracker.model.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.io.IOException;
import java.util.ArrayList;

/**
 * The issue service file used to handle all requests to the issue class.
 *
 * @author Alyssa Heimlicher
 * @version 1.0
 * @since 2022-05-23
 */
@Path("/issue")
public class IssueResource {

    /**
     * Gets all issues from the JSON file.
     *
     * @return an arraylist of issues
     * @author Alyssa Heimlicher
     */
    @GET
    @Produces("application/json")
    @Path("/list")
    public Response getAllIssues() {
        try {
            ArrayList<Issue> issues = new DataHandlerGen<>(Issue.class).getArrayListOutOfJSON("issueJSON");
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.findAndRegisterModules();
            return Response.status(200).entity(objectMapper.writeValueAsString(issues)).build();
        } catch (IOException e) {
            e.printStackTrace();
            return Response.status(500).entity("{\"error\":\"" + e.getMessage() + "\"}").build();
        }
    }

    /**
     * Gets an issue from the JSON file based on the uuid.
     *
     * @param uuid the uuid of the issue
     * @return the issue
     * @author Alyssa Heimlicher
     */
    @GET
    @Produces("application/json")
    @Path("/{uuid}")
    public Response getSingleIssueByUUID(@PathParam("uuid") String uuid) {
        try {
            Issue issue = new IssueDataHandler().readIssueByUUID(uuid);
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());
            objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
            if (issue == null) {
                return Response.status(404).entity("{\"error\":\"Issue not found\"}").build();
            }

            return Response.status(200).entity(objectMapper.writeValueAsString(issue)).build();
        } catch (IOException | NoSuchFieldException | IllegalAccessException e) {
            return Response.status(500).entity("{\"error\":\"" + e.getMessage() + "\"}").build();
        }
    }

    /**
     * This method creates a new issue and adds it to the json file.
     *
     * @param issue the issue to be added
     * @return a status code of 200 if the issue was added
     */
    @RolesAllowed({"admin", "user"})
    @POST
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/create")
    public Response insertIssue(@Valid @BeanParam Issue issue,
                                @FormParam("projectUUID") String projectUUID,
                                ContainerRequestContext requestContext) {
        User user;
        try {
            user = TokenHandler.getUserFromCookie(requestContext);
        } catch (NotLoggedInException | IOException e) {
            return Response.status(Response.Status.UNAUTHORIZED).entity("{\"error\":\"" + e.getMessage() + "\"}").build();
        }

        ProjectDatahandler projectDatahandler = new ProjectDatahandler();
        Project project;
        try {
            project = projectDatahandler.getSingleFromJsonArray(projectUUID);
            if ("admin".equalsIgnoreCase(user.getUserRole()) || project.getUser().getUserUUID().equals(user.getUserUUID())) {
                DataHandlerGen<Issue> dh = new DataHandlerGen<>(Issue.class);
                dh.insertIntoJson(issue, "issueJSON");
                ArrayList<Issue> issues = project.getIssues();
                issues.add(issue);
                project.setIssues(issues);
                projectDatahandler.updateSingleFromJson("projectJSON", "projectUUID", projectUUID, project);

                return Response
                        .status(200)
                        .entity("")
                        .build();
            } else {
                return Response.status(Response.Status.UNAUTHORIZED).entity("{\"error\":\"You do not have permission to create this issue\"}").build();
            }
        } catch (IOException | NoSuchFieldException | IllegalAccessException e) {
            return Response.status(500).entity("{\"error\":\"" + e.getMessage() + "\"}").build();
        }


    }

    /**
     * This method deletes an issue from the json file based on the uuid.
     *
     * @param uuid the uuid of the issue
     * @return a response based on if the issue was deleted or not
     * @author Alyssa Heimlicher
     */
    @RolesAllowed({"admin"})
    @DELETE
    @Produces("application/json")
    @Path("/delete/{uuid}")
    public Response deleteIssueByUUID(@PathParam("uuid") String uuid, ContainerRequestContext requestContext) {
        User user;
        try {
            user = TokenHandler.getUserFromCookie(requestContext);
        } catch (NotLoggedInException | IOException e) {
            return Response.status(Response.Status.UNAUTHORIZED).entity("{\"error\":\"" + e.getMessage() + "\"}").build();
        }

        if ("admin".equalsIgnoreCase(user.getUserRole())) {
            try {
                ProjectDatahandler projectDatahandler = new ProjectDatahandler();
                Project project = projectDatahandler.getProjectByObjectUUID(uuid, "issue");
                if(project == null){
                    return Response.status(Response.Status.NOT_FOUND).entity("{\"error\":\"project with issue not found\"}").build();
                }
                ArrayList<Issue> issues = project.getIssues();
                issues.removeIf(issue -> issue.getIssueUUID().equals(uuid));
                project.setIssues(issues);
                projectDatahandler.updateSingleFromJson("projectJSON", "projectUUID", project.getProjectUUID(), project);
                new IssueDataHandler().deleteSingleFromJson("issueJSON", "issueUUID", uuid);
                return Response.status(200).entity("{\"success\":\"Issue deleted\"}").build();
            } catch (IOException | NoSuchFieldException | IllegalAccessException e) {
                return Response.status(500).entity("{\"error\":\"" + e.getMessage() + "\"}").build();
            } catch (IllegalArgumentException e) {
                return Response.status(404).entity("{\"error\":\"Issue not found\"}").build();
            }
        }
        return Response.status(Response.Status.UNAUTHORIZED).entity("{\"error\":\"You do not have permission to delete this issue\"}").build();

    }

    /**
     * This method updates an issue in the json file based on the uuid.
     *
     * @param uuid  the uuid of the issue
     * @param issue the issue to be updated
     * @return a response based on if the issue was updated or not
     * @throws IOException            if the json file cannot be read
     * @throws NoSuchFieldException   if the field cannot be found
     * @throws IllegalAccessException if the fields cannot be accessed
     * @author Alyssa Heimlicher
     */
    @RolesAllowed({"admin", "user"})
    @PUT
    @Produces("application/json")
    @Path("/update/{uuid}")
    public Response updateIssue(@PathParam("uuid") String uuid, @Valid @BeanParam Issue issue, ContainerRequestContext requestContext) throws IOException, NoSuchFieldException, IllegalAccessException {
        User user;
        try {
            user = TokenHandler.getUserFromCookie(requestContext);
        } catch (NotLoggedInException | IOException e) {
            return Response.status(Response.Status.UNAUTHORIZED).entity("{\"error\":\"" + e.getMessage() + "\"}").build();
        }
        boolean changed = false;
        Issue toBeUpdatedIssue = new IssueDataHandler().readIssueByUUID(uuid);
        if (toBeUpdatedIssue == null) {
            return Response.status(404).entity("{\"error\":\"Issue not found\"}").build();
        }

        if (issue.getTitle() != null && !issue.getTitle().equals(toBeUpdatedIssue.getTitle())) {
            toBeUpdatedIssue.setTitle(issue.getTitle());
            changed = true;
        }

        if (issue.getDescription() != null && !issue.getDescription().equals(toBeUpdatedIssue.getDescription())) {
            toBeUpdatedIssue.setDescription(issue.getDescription());
            changed = true;
        }

        if (issue.getStatus() != null && !issue.getStatus().equals(toBeUpdatedIssue.getStatus())) {
            toBeUpdatedIssue.setStatus(issue.getStatus());
            changed = true;
        }

        if (issue.getSeverity() != null && !issue.getSeverity().equals(toBeUpdatedIssue.getSeverity())) {
            toBeUpdatedIssue.setSeverity(issue.getSeverity());
            changed = true;
        }

        ProjectDatahandler projectDatahandler = new ProjectDatahandler();
        Project project = projectDatahandler.getProjectByObjectUUID(uuid, "issue");
        if (project == null) {
            return Response.status(404).entity("{\"error\":\"Project with issue not found\"}").build();
        }
        if (user.getUserRole().equalsIgnoreCase("admin") || project.getUser().getUserUUID().equals(user.getUserUUID())) {
            if (changed) {
                new IssueDataHandler().updateSingleFromJson("issueJSON", "issueUUID", uuid, toBeUpdatedIssue);
                ArrayList<Issue> issues = project.getIssues();
                for (int i = 0; i < issues.size(); i++) {
                    if (issues.get(i).getIssueUUID().equals(uuid)) {
                        issues.set(i, toBeUpdatedIssue);
                        break;
                    }
                }
                project.setIssues(issues);
                projectDatahandler.updateSingleFromJson("projectJSON", "projectUUID", project.getProjectUUID(), project);
                return Response.status(200).entity("{\"success\":\"Issue updated\"}").build();
            } else {
                return Response.status(200).entity("{\"success\":\"No changes made\"}").build();
            }
        } else {
            return Response.status(Response.Status.UNAUTHORIZED).entity("{\"error\":\"You do not have permission to update this issue\"}").build();
        }

    }
}
