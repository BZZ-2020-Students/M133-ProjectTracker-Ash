package com.example.projecttracker.services;

import com.example.projecttracker.data.DataHandlerGen;
import com.example.projecttracker.data.PatchnoteDataHandler;
import com.example.projecttracker.model.PatchNote;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
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
     * @author Alyssa Heimlicher
     */
    @POST
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/create")
    public Response createPatchNote(@Valid @BeanParam PatchNote patchNote) {
        new PatchnoteDataHandler().insertIntoJson(patchNote, "patchNoteJSON");

        return Response
                .status(200)
                .entity("")
                .build();
    }

    /**
     * This method deletes a patchNote from the json file by its uuid.
     *
     * @param uuid the uuid of the patch note.
     * @return a response with the status code
     * @author Alyssa Heimlicher
     */
    @DELETE
    @Produces("application/json")
    @Path("/delete/{uuid}")
    public Response deletePatchNoteByUUID(@PathParam("uuid") String uuid) {
        try {
            new PatchnoteDataHandler().deleteSingleFromJson("patchNoteJSON", "patchNoteUUID", uuid);
            return Response.status(200).entity("{\"success\":\"PatchNote deleted\"}").build();
        } catch (IOException | NoSuchFieldException | IllegalAccessException e) {
            return Response.status(500).entity("{\"error\":\"" + e.getMessage() + "\"}").build();
        } catch (IllegalArgumentException e) {
            return Response.status(404).entity("{\"error\":\"PatchNote not found\"}").build();
        }
    }

    /**
     * This method updates a patchNote from the json file by its uuid.
     *
     * @param uuid the uuid of the patch note.
     * @param patchNote the patch note to be updated.
     * @return a response with the status code
     * @throws IOException if the json file cannot be read.
     * @throws NoSuchFieldException if the field is not found.
     * @throws IllegalAccessException if the file is not accessible.
     * @author Alyssa Heimlicher
     *
     */
    @PUT
    @Produces("application/json")
    @Path("/update/{uuid}")
    public Response updatePatchNote(@PathParam("uuid") String uuid, @Valid @BeanParam PatchNote patchNote) throws IOException, NoSuchFieldException, IllegalAccessException {
        boolean changed = false;
        PatchNote toBeUpdatedPatchNote = new PatchnoteDataHandler().readPatchNoteByUUID(uuid);
        if (toBeUpdatedPatchNote == null) {
            return Response.status(404).entity("{\"error\":\"PatchNote not found\"}").build();
        }

        if(patchNote.getTitle() != null && !patchNote.getTitle().equals(toBeUpdatedPatchNote.getTitle())) {
            toBeUpdatedPatchNote.setTitle(patchNote.getTitle());
            changed = true;
        }

        if(patchNote.getDescription() != null && !patchNote.getDescription().equals(toBeUpdatedPatchNote.getDescription())) {
            toBeUpdatedPatchNote.setDescription(patchNote.getDescription());
            changed = true;
        }

        if(patchNote.getVersion() != null && !patchNote.getVersion().equals(toBeUpdatedPatchNote.getVersion())) {
            toBeUpdatedPatchNote.setVersion(patchNote.getVersion());
            changed = true;
        }

        if(changed){
            new PatchnoteDataHandler().updateSingleFromJson("patchNoteJSON", "patchNoteUUID", uuid, toBeUpdatedPatchNote);
            return Response.status(200).entity("{\"success\":\"PatchNote updated\"}").build();
        }

        return Response.status(200).entity("{\"success\":\"No changes made\"}").build();

    }


}
