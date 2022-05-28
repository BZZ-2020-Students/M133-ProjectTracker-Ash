package com.example.projecttracker.services;

import com.example.projecttracker.data.DataHandlerGen;
import com.example.projecttracker.data.IssueDataHandler;
import com.example.projecttracker.model.Issue;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
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
     * Gets an issue from the JSON file based on the id.
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
            objectMapper.findAndRegisterModules();
            if (issue == null) {
                return Response.status(404).entity("{\"error\":\"Issue not found\"}").build();
            }

            return Response.status(200).entity(objectMapper.writeValueAsString(issue)).build();
        } catch (IOException | NoSuchFieldException | IllegalAccessException e) {
            return Response.status(500).entity("{\"error\":\"" + e.getMessage() + "\"}").build();
        }
    }
}
