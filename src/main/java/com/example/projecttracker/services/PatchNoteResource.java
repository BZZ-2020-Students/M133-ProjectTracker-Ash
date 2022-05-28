package com.example.projecttracker.services;

import com.example.projecttracker.data.DataHandlerGen;
import com.example.projecttracker.data.PatchnoteDataHandler;
import com.example.projecttracker.model.PatchNote;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.UUID;


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
            objectMapper.findAndRegisterModules();
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
     * @param title       the title of the patch note.
     * @param description the description of the patch note.
     * @param date        the date of the patch note.
     * @param version     the version of the patch note.
     * @return a response depending on the success of the operation.
     * @author Alyssa Heimlicher
     */
    @POST
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/create")
    public Response createPatchNote(@FormParam("title") String title,
                                    @FormParam("description") String description,
                                    @FormParam("date") String date,
                                    @FormParam("version") String version) {
        LocalDate dateAsLocalDate = LocalDate.parse(date);
        String patchNoteUUID = UUID.randomUUID().toString();
        PatchNote patchNote = new PatchNote(patchNoteUUID, title, description, dateAsLocalDate, version);
        new PatchnoteDataHandler().insertIntoJson(patchNote, "patchNoteJSON");

        return Response
                .status(200)
                .entity("")
                .build();
    }

}
