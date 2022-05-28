package com.example.projecttracker.services;

import com.example.projecttracker.data.DataHandlerGen;
import com.example.projecttracker.data.IssueDataHandler;
import com.example.projecttracker.model.Issue;
import com.example.projecttracker.model.Status;
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
     * @param title       the title of the issue
     * @param description the description of the issue
     * @param severity    the severity of the issue
     * @return a response
     * @author Alyssa Heimlicher
     */
    @POST
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/create")
    public Response insertIssue(@FormParam("title") String title,
                                @FormParam("description") String description,
                                @FormParam("severity") String severity) {
        System.out.println("IssueResource.insertIssue");
        String issueUUID = UUID.randomUUID().toString();
        Issue issue = new Issue(issueUUID, title, description, severity, Status.TODO);
        DataHandlerGen<Issue> dh = new DataHandlerGen<>(Issue.class);
        dh.insertIntoJson(issue, "issueJSON");

        return Response
                .status(201)
                .entity("")
                .build();
    }

    /**
     * This method deletes an issue from the json file based on the uuid.
     *
     * @param uuid the uuid of the issue
     * @return a response based on if the issue was deleted or not
     * @author Alyssa Heimlicher
     */
    @DELETE
    @Produces("application/json")
    @Path("/delete/{uuid}")
    public Response deleteIssueByUUID(@PathParam("uuid") String uuid) {
        try {
            new IssueDataHandler().deleteSingleFromJson("issueJSON", "issueUUID", uuid);
            return Response.status(200).entity("{\"success\":\"Issue deleted\"}").build();
        } catch (IOException | NoSuchFieldException | IllegalAccessException e) {
            return Response.status(500).entity("{\"error\":\"" + e.getMessage() + "\"}").build();
        } catch (IllegalArgumentException e) {
            return Response.status(404).entity("{\"error\":\"Issue not found\"}").build();
        }
    }
}
