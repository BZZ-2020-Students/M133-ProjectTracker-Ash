package com.example.projecttracker.services;

import com.example.projecttracker.data.TaskDataHandler;
import com.example.projecttracker.model.Task;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Response;

import java.io.IOException;
import java.util.ArrayList;


/**
 * This class is responsible for handling all requests to the task class.
 *
 * @author Alyssa Heimlicher
 * @version 1.0
 * @since 2022-05-23
 */
@Path("/task")
public class TaskResource {

    /**
     * This method gets all tasks from the json file.
     *
     * @return an arraylist of tasks
     * @author Alyssa Heimlicher
     */
    @GET
    @Produces("application/json")
    @Path("/list")
    public Response getAllTasks() {
        try {
            ArrayList<Task> tasks = new TaskDataHandler().getArrayListOutOfJSON();
            ObjectMapper objectMapper = new ObjectMapper();
            return Response.status(200).entity(objectMapper.writeValueAsString(tasks)).build();
        } catch (IOException e) {
            e.printStackTrace();
            return Response.status(500).entity("{\"error\":\"" + e.getMessage() + "\"}").build();
        }
    }

    /**
     * This method gets a task from the json file by its id.
     *
     * @param id the id of the task
     * @return a task with the id
     * @author Alyssa Heimlicher
     */
    @GET
    @Produces("application/json")
    @Path("/{id}")
    public Response getSingleTaskByID(@PathParam("id") int id) {
        try {
            Task task = new TaskDataHandler().readTaskById(id);
            ObjectMapper objectMapper = new ObjectMapper();

            if (task == null) {
                return Response.status(404).entity("{\"error\":\"Task not found\"}").build();
            }

            return Response.status(200).entity(objectMapper.writeValueAsString(task)).build();
        } catch (IOException | NoSuchFieldException | IllegalAccessException e) {
            return Response.status(500).entity("{\"error\":\"" + e.getMessage() + "\"}").build();
        }
    }
}