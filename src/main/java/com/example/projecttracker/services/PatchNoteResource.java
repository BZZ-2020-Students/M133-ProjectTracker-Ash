package com.example.projecttracker.services;

import com.example.projecttracker.data.DataHandlerGen;
import com.example.projecttracker.model.PatchNote;
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
            return Response.status(200).entity(objectMapper.writeValueAsString(patchNotes)).build();
        } catch (IOException e) {
            e.printStackTrace();
            return Response.status(500).entity("{\"error\":\"" + e.getMessage() + "\"}").build();
        }
    }

    /**
     * gets a patch note by its id.
     * @param id the id of the patch note.
     * @return the patch note.
     * @author Alyssa Heimlicher
     * */
    @GET
    @Produces("application/json")
    @Path("/{id}")
    public Response getSinglePatchNoteByID(@PathParam("id") int id) {
        try {
            PatchNote patchNote = new DataHandlerGen<>(PatchNote.class).getSingleFromJsonArray("patchNoteJSON", "patchNoteId", id);
            ObjectMapper objectMapper = new ObjectMapper();

            if (patchNote == null) {
                return Response.status(404).entity("{\"error\":\"PatchNote not found\"}").build();
            }

            return Response.status(200).entity(objectMapper.writeValueAsString(patchNote)).build();
        } catch (IOException | NoSuchFieldException | IllegalAccessException e) {
            return Response.status(500).entity("{\"error\":\"" + e.getMessage() + "\"}").build();
        }
    }
}
