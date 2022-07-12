package com.example.projecttracker.services;

import com.example.projecttracker.authentication.NotLoggedInException;
import com.example.projecttracker.authentication.TokenHandler;
import com.example.projecttracker.data.DataHandlerGen;
import com.example.projecttracker.data.PatchnoteDataHandler;
import com.example.projecttracker.data.ProjectDatahandler;
import com.example.projecttracker.model.PatchNote;
import com.example.projecttracker.model.Project;
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
@Path("/patchnote")
public class PatchNoteResource {

    /**
     * Gets all patch notes from the json file.
     *
     * @return A list of all patch notes.
     * @author Alyssa Heimlicher
     */
    @GET
    @Produces("application/json")
    @Path("/list")
    public Response getAllPatchNotes() {
        try {
            ArrayList<PatchNote> patchNotes = new DataHandlerGen<>(PatchNote.class).getArrayListOutOfJSON("patchNoteJSON");
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());
            objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
            return Response.status(200).entity(objectMapper.writeValueAsString(patchNotes)).build();
        } catch (IOException e) {
            e.printStackTrace();
            return Response.status(500).entity("{\"error\":\"" + e.getMessage() + "\"}").build();
        }
    }

    /**
     * gets a patch note by its uuid.
     *
     * @param uuid the uuid of the patch note.
     * @return the patch note.
     * @author Alyssa Heimlicher
     */
    @GET
    @Produces("application/json")
    @Path("/{uuid}")
    public Response getSinglePatchNoteByID(@PathParam("uuid") String uuid) {
        try {
            PatchNote patchNote = new PatchnoteDataHandler().readPatchNoteByUUID(uuid);
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());
            objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
            if (patchNote == null) {
                return Response.status(404).entity("{\"error\":\"PatchNote not found\"}").build();
            }

            return Response.status(200).entity(objectMapper.writeValueAsString(patchNote)).build();
        } catch (IOException | NoSuchFieldException | IllegalAccessException e) {
            return Response.status(500).entity("{\"error\":\"" + e.getMessage() + "\"}").build();
        }
    }

    /**
     * Creates a new patch note and adds it to the json file.
     *
     * @param patchNote the patch note to be added.
     * @return a response.
     * @author Alyssa Heimlicher
     */
    @RolesAllowed({"admin", "user"})
    @POST
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/create")
    public Response createPatchNote(@Valid @BeanParam PatchNote patchNote, @FormParam("projectUUID") String projectUUID, ContainerRequestContext requestContext) {
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

                new PatchnoteDataHandler().insertIntoJson(patchNote, "patchNoteJSON");
                ArrayList<PatchNote> patchNotes = project.getPatchNotes();
                patchNotes.add(patchNote);
                project.setPatchNotes(patchNotes);
                projectDatahandler.updateSingleFromJson("projectJSON", "projectUUID", projectUUID, project);

                return Response
                        .status(200)
                        .entity("")
                        .build();
            } else {
                return Response.status(Response.Status.UNAUTHORIZED).entity("{\"error\":\"You do not have permission to create this Patchnote\"}").build();
            }
        } catch (IOException | NoSuchFieldException | IllegalAccessException e) {
            return Response.status(500).entity("{\"error\":\"" + e.getMessage() + "\"}").build();
        }
    }

    /**
     * This method deletes a patchNote from the json file by its uuid.
     *
     * @param uuid the uuid of the patch note.
     * @return a response with the status code
     * @author Alyssa Heimlicher
     */
    @RolesAllowed({"admin"})
    @DELETE
    @Produces("application/json")
    @Path("/delete/{uuid}")
    public Response deletePatchNoteByUUID(@PathParam("uuid") String uuid, ContainerRequestContext requestContext) {
        User user;
        try {
            user = TokenHandler.getUserFromCookie(requestContext);
        } catch (NotLoggedInException | IOException e) {
            return Response.status(Response.Status.UNAUTHORIZED).entity("{\"error\":\"" + e.getMessage() + "\"}").build();
        }
        if (user.getUserRole().equalsIgnoreCase("admin")) {


            try {
                new PatchnoteDataHandler().deleteSingleFromJson("patchNoteJSON", "patchNoteUUID", uuid);
                return Response.status(200).entity("{\"success\":\"PatchNote deleted\"}").build();
            } catch (IOException | NoSuchFieldException | IllegalAccessException e) {
                return Response.status(500).entity("{\"error\":\"" + e.getMessage() + "\"}").build();
            } catch (IllegalArgumentException e) {
                return Response.status(404).entity("{\"error\":\"PatchNote not found\"}").build();
            }
        }
        return Response.status(Response.Status.UNAUTHORIZED).entity("{\"error\":\"You do not have permission to delete this Patchnote\"}").build();
    }

    /**
     * This method updates a patchNote from the json file by its uuid.
     *
     * @param uuid      the uuid of the patch note.
     * @param patchNote the patch note to be updated.
     * @return a response with the status code
     * @throws IOException            if the json file cannot be read.
     * @throws NoSuchFieldException   if the field is not found.
     * @throws IllegalAccessException if the file is not accessible.
     * @author Alyssa Heimlicher
     */
    @RolesAllowed({"admin", "user"})
    @PUT
    @Produces("application/json")
    @Path("/update/{uuid}")
    public Response updatePatchNote(@PathParam("uuid") String uuid, @Valid @BeanParam PatchNote patchNote, ContainerRequestContext requestContext) throws IOException, NoSuchFieldException, IllegalAccessException {
        User user;
        try {
            user = TokenHandler.getUserFromCookie(requestContext);
        } catch (NotLoggedInException | IOException e) {
            return Response.status(Response.Status.UNAUTHORIZED).entity("{\"error\":\"" + e.getMessage() + "\"}").build();
        }

        boolean changed = false;
        PatchNote toBeUpdatedPatchNote = new PatchnoteDataHandler().readPatchNoteByUUID(uuid);
        if (toBeUpdatedPatchNote == null) {
            return Response.status(404).entity("{\"error\":\"PatchNote not found\"}").build();
        }

        if (patchNote.getTitle() != null && !patchNote.getTitle().equals(toBeUpdatedPatchNote.getTitle())) {
            toBeUpdatedPatchNote.setTitle(patchNote.getTitle());
            changed = true;
        }

        if (patchNote.getDescription() != null && !patchNote.getDescription().equals(toBeUpdatedPatchNote.getDescription())) {
            toBeUpdatedPatchNote.setDescription(patchNote.getDescription());
            changed = true;
        }

        if (patchNote.getVersion() != null && !patchNote.getVersion().equals(toBeUpdatedPatchNote.getVersion())) {
            toBeUpdatedPatchNote.setVersion(patchNote.getVersion());
            changed = true;
        }

        ProjectDatahandler projectDatahandler = new ProjectDatahandler();
        Project project = projectDatahandler.getProjectByObjectUUID(uuid, "patchnote");
        if (project == null) {
            return Response.status(404).entity("{\"error\":\"Project with patchnote  not found\"}").build();
        }
        if (user.getUserRole().equalsIgnoreCase("admin") || project.getUser().getUserUUID().equals(user.getUserUUID())) {
            if (changed) {
                new PatchnoteDataHandler().updateSingleFromJson("patchNoteJSON", "patchNoteUUID", uuid, toBeUpdatedPatchNote);
                ArrayList<PatchNote> patchNotes = project.getPatchNotes();
                for (int i = 0; i < patchNotes.size(); i++) {
                    if (patchNotes.get(i).getPatchNoteUUID().equals(uuid)) {
                        patchNotes.set(i, toBeUpdatedPatchNote);
                        break;
                    }
                }
                project.setPatchNotes(patchNotes);
                projectDatahandler.updateSingleFromJson("projectJSON", "projectUUID", project.getProjectUUID(), project);
                return Response.status(200).entity("{\"success\":\"PatchNote updated\"}").build();
            } else {
                return Response.status(200).entity("{\"success\":\"No changes made\"}").build();
            }
        } else {
            return Response.status(Response.Status.UNAUTHORIZED).entity("{\"error\":\"You do not have permission to update this Patchnote\"}").build();
        }
    }


}
