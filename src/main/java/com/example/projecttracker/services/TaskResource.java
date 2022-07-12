package com.example.projecttracker.services;

import com.example.projecttracker.authentication.NotLoggedInException;
import com.example.projecttracker.authentication.TokenHandler;
import com.example.projecttracker.data.DataHandlerGen;
import com.example.projecttracker.data.ProjectDatahandler;
import com.example.projecttracker.data.TaskDataHandler;
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
import java.time.LocalDate;
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
     * @param task the task to be added
     * @return a response
     * @author Alyssa Heimlicher
     */
    @RolesAllowed({"admin", "user"})
    @POST
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/create")
    public Response insertTask(@Valid @BeanParam Task task, @FormParam("projectUUID") String projectUUID, ContainerRequestContext requestContext) {
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
                LocalDate deadlineLocal = LocalDate.parse(task.getTempDate());
                task.setDeadline(deadlineLocal);
                DataHandlerGen<Task> dh = new DataHandlerGen<>(Task.class);
                dh.insertIntoJson(task, "taskJSON");
                ArrayList<Task> tasks = project.getTasks();
                tasks.add(task);
                project.setTasks(tasks);
                projectDatahandler.updateSingleFromJson("projectJSON", "projectUUID", projectUUID, project);

                return Response
                        .status(200)
                        .entity("")
                        .build();
            } else {
                return Response.status(Response.Status.UNAUTHORIZED).entity("{\"error\":\"You do not have permission to create this task\"}").build();
            }
        } catch (IOException | NoSuchFieldException | IllegalAccessException e) {
            return Response.status(500).entity("{\"error\":\"" + e.getMessage() + "\"}").build();
        }
    }

    /**
     * This method deletes a task from the json file by its uuid.
     *
     * @param uuid the uuid of the task
     * @return a response with the status code
     * @author Alyssa Heimlicher
     */
    @RolesAllowed({"admin"})
    @DELETE
    @Produces("application/json")
    @Path("/delete/{uuid}")
    public Response deleteTaskByUUID(@PathParam("uuid") String uuid, ContainerRequestContext requestContext) {
        User user;
        try {
            user = TokenHandler.getUserFromCookie(requestContext);
        } catch (NotLoggedInException | IOException e) {
            return Response.status(Response.Status.UNAUTHORIZED).entity("{\"error\":\"" + e.getMessage() + "\"}").build();
        }
        if ("admin".equalsIgnoreCase(user.getUserRole())) {
            try {
                //delete task from project
                ProjectDatahandler projectDatahandler = new ProjectDatahandler();
                Project project = projectDatahandler.getProjectByObjectUUID(uuid, "task");
                if (project == null) {
                    return Response.status(Response.Status.NOT_FOUND).entity("{\"error\":\"project with task not found\"}").build();
                }
                ArrayList<Task> tasks = project.getTasks();
               tasks.removeIf(task -> task.getTaskUUID().equals(uuid));
                project.setTasks(tasks);
                projectDatahandler.updateSingleFromJson("projectJSON", "projectUUID", project.getProjectUUID(), project);
                new TaskDataHandler().deleteSingleFromJson("taskJSON", "taskUUID", uuid);
                return Response.status(200).entity("{\"success\":\"Task deleted\"}").build();
            } catch (IOException | NoSuchFieldException | IllegalAccessException e) {
                return Response.status(500).entity("{\"error\":\"" + e.getMessage() + "\"}").build();
            } catch (IllegalArgumentException e) {
                return Response.status(404).entity("{\"error\":\"Task not found\"}").build();
            }
        }
        return Response.status(Response.Status.UNAUTHORIZED).entity("{\"error\":\"You do not have permission to delete this task\"}").build();

    }

    /**
     * This method updates a task in the json file.
     *
     * @param uuid the uuid of the task
     * @param task the task to be updated
     * @return a response based on if the task was updated or not
     * @throws IOException            if the json file cannot be read
     * @throws NoSuchFieldException   if the field cannot be found
     * @throws IllegalAccessException if the file cannot be accessed
     * @author Alyssa Heimlicher
     */
    @PUT
    @Produces("application/json")
    @Path("/update/{uuid}")
    public Response updateTask(@PathParam("uuid") String uuid, @Valid @BeanParam Task task, ContainerRequestContext requestContext) throws IOException, NoSuchFieldException, IllegalAccessException {
        User user;
        try {
            user = TokenHandler.getUserFromCookie(requestContext);
        } catch (NotLoggedInException | IOException e) {
            return Response.status(Response.Status.UNAUTHORIZED).entity("{\"error\":\"" + e.getMessage() + "\"}").build();
        }

        boolean changed = false;
        Task toBeUpdatedTask = new TaskDataHandler().readTaskByUUID(uuid);
        if (toBeUpdatedTask == null) {
            return Response.status(404).entity("{\"error\":\"Task not found\"}").build();
        }

        if (task.getTitle() != null && !task.getTitle().equals(toBeUpdatedTask.getTitle())) {
            toBeUpdatedTask.setTitle(task.getTitle());
            changed = true;
        }

        if (task.getDescription() != null && !task.getDescription().equals(toBeUpdatedTask.getDescription())) {
            toBeUpdatedTask.setDescription(task.getDescription());
            changed = true;
        }

        if (task.getDeadline() != null && !task.getDeadline().equals(toBeUpdatedTask.getDeadline())) {
            toBeUpdatedTask.setDeadline(task.getDeadline());
            changed = true;
        }

        if (task.getStatus() != null && !task.getStatus().equals(toBeUpdatedTask.getStatus())) {
            toBeUpdatedTask.setStatus(task.getStatus());
            changed = true;
        }
        ProjectDatahandler projectDatahandler = new ProjectDatahandler();
        Project project = projectDatahandler.getProjectByObjectUUID(uuid, "task");
        if (project == null) {
            return Response.status(404).entity("{\"error\":\"Project with task not found\"}").build();
        }
        if (user.getUserRole().equalsIgnoreCase("admin") || project.getUser().getUserUUID().equals(user.getUserUUID())) {
            if (changed) {
                new TaskDataHandler().updateSingleFromJson("taskJSON", "taskUUID", uuid, toBeUpdatedTask);
                ArrayList<Task> tasks = project.getTasks();
                for (int i = 0; i < tasks.size(); i++) {
                    if (tasks.get(i).getTaskUUID().equals(uuid)) {
                        tasks.set(i, toBeUpdatedTask);
                        break;
                    }
                }
                project.setTasks(tasks);
                projectDatahandler.updateSingleFromJson("projectJSON", "projectUUID", project.getProjectUUID(), project);

                return Response.status(200).entity("{\"success\":\"Task updated\"}").build();
            } else {
                return Response.status(200).entity("{\"success\":\"No changes made\"}").build();
            }
        } else {
            return Response.status(Response.Status.UNAUTHORIZED).entity("{\"error\":\"You do not have permission to update this task\"}").build();
        }
    }
}