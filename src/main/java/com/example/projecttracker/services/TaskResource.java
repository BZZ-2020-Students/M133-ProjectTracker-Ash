package com.example.projecttracker.services;

import com.example.projecttracker.data.DataHandlerGen;
import com.example.projecttracker.data.TaskDataHandler;
import com.example.projecttracker.model.Status;
import com.example.projecttracker.model.Task;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;


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
            objectMapper.registerModule(new JavaTimeModule());
            objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
            return Response.status(200).entity(objectMapper.writeValueAsString(tasks)).build();
        } catch (IOException e) {
            e.printStackTrace();
            return Response.status(500).entity("{\"error\":\"" + e.getMessage() + "\"}").build();
        }
    }

    /**
     * This method gets a task from the json file by its uuid.
     *
     * @param uuid the uuid of the task
     * @return a task with the id
     * @author Alyssa Heimlicher
     */
    @GET
    @Produces("application/json")
    @Path("/{uuid}")
    public Response getSingleTaskByID(@PathParam("uuid") String uuid) {
        try {
            Task task = new TaskDataHandler().readTaskByUUID(uuid);
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());
            objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
            if (task == null) {
                return Response.status(404).entity("{\"error\":\"Task not found\"}").build();
            }

            return Response.status(200).entity(objectMapper.writeValueAsString(task)).build();
        } catch (IOException | NoSuchFieldException | IllegalAccessException e) {
            return Response.status(500).entity("{\"error\":\"" + e.getMessage() + "\"}").build();
        }
    }


    /**
     * This method creates a new task and adds it to the json file.
     *
     * @param title       the title of the task
     * @param description the description of the task
     * @param deadline    the deadline of the task
     * @return a response
     * @author Alyssa Heimlicher
     */
    @POST
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/create")
    public Response insertTask(@FormParam("title") String title,
                               @FormParam("description") String description,
                               @FormParam("deadline") String deadline) {
        System.out.println("TaskResource.insertTask");
        LocalDate deadlineLocal = LocalDate.parse(deadline);
        String taskUUID = UUID.randomUUID().toString();
        Task task = new Task(taskUUID, title, description, deadlineLocal, Status.TODO);
        DataHandlerGen<Task> dh = new DataHandlerGen<>(Task.class);
        dh.insertIntoJson(task, "taskJSON");

        return Response
                .status(200)
                .entity("")
                .build();
    }

    /**
     * This method deletes a task from the json file by its uuid.
     *
     * @param uuid the uuid of the task
     * @return a response with the status code
     */
    @DELETE
    @Produces("application/json")
    @Path("/delete/{uuid}")
    public Response deleteTaskByUUID(@PathParam("uuid") String uuid) {
        try {
            new TaskDataHandler().deleteSingleFromJson("taskJSON", "taskUUID", uuid);
            return Response.status(200).entity("{\"success\":\"Task deleted\"}").build();
        } catch (IOException | NoSuchFieldException | IllegalAccessException e) {
            return Response.status(500).entity("{\"error\":\"" + e.getMessage() + "\"}").build();
        } catch (IllegalArgumentException e) {
            return Response.status(404).entity("{\"error\":\"Task not found\"}").build();
        }
    }
}