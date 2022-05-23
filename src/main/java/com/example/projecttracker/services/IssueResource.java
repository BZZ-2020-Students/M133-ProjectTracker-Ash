package com.example.projecttracker.services;

import com.example.projecttracker.data.DataHandlerGen;
import com.example.projecttracker.model.Issue;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Response;

import java.io.IOException;
import java.util.ArrayList;

@Path("/issue")
public class IssueResource {
    @GET
    @Produces("application/json")
    @Path("/list")
    public Response getAllIssues() {
        try {
            ArrayList<Issue> issues = new DataHandlerGen<>(Issue.class).getArrayListOutOfJSON("issueJSON");
            ObjectMapper objectMapper = new ObjectMapper();
            return Response.status(200).entity(objectMapper.writeValueAsString(issues)).build();
        } catch (IOException e) {
            e.printStackTrace();
            return Response.status(500).entity("{\"error\":\"" + e.getMessage() + "\"}").build();
        }
    }

    @GET
    @Produces("application/json")
    @Path("/{id}")
    public Response getSingleTaskByID(@PathParam("id") int id) {
        try {
            Issue issue = new DataHandlerGen<>(Issue.class).getSingleFromJsonArray("issueJSON", "issueId", id);
            ObjectMapper objectMapper = new ObjectMapper();

            if (issue == null) {
                return Response.status(404).entity("{\"error\":\"Issue not found\"}").build();
            }

            return Response.status(200).entity(objectMapper.writeValueAsString(issue)).build();
        } catch (IOException | NoSuchFieldException | IllegalAccessException e) {
            return Response.status(500).entity("{\"error\":\"" + e.getMessage() + "\"}").build();
        }
    }
}
