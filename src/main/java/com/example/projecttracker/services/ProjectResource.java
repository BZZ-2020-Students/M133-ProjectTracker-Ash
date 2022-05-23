package com.example.projecttracker.services;

import com.example.projecttracker.data.ProjectDatahandler;
import com.example.projecttracker.model.Project;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Response;

import java.io.IOException;
import java.util.ArrayList;

@Path("/project")
public class ProjectResource {
    @GET
    @Produces("application/json")
    @Path("/list")
    public Response getAllProjects() {
        try {
            ArrayList<Project> projects = new ProjectDatahandler().getArrayListOutOfJSON();
            ObjectMapper objectMapper = new ObjectMapper();
            return Response.status(200).entity(objectMapper.writeValueAsString(projects)).build();
        } catch (IOException | NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
            return Response.status(500).entity("{\"error\":\"" + e.getMessage() + "\"}").build();
        }
    }

    @GET
    @Produces("application/json")
    @Path("/{id}")
    public Response getSingleProjectByID(@PathParam("id") int id) {
        try {
            Project project = new ProjectDatahandler().getSingleFromJsonArray(id);

            if (project == null) {
                return Response.status(404).entity("{\"error\":\"Project not found\"}").build();
            }

            ObjectMapper objectMapper = new ObjectMapper();
            return Response.status(200).entity(objectMapper.writeValueAsString(project)).build();
        } catch (IOException | NoSuchFieldException | IllegalAccessException e) {
            return Response.status(500).entity("{\"error\":\"" + e.getMessage() + "\"}").build();
        }
    }
}
