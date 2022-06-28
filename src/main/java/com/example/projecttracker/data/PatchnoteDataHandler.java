package com.example.projecttracker.data;

import com.example.projecttracker.model.PatchNote;

import java.io.IOException;
import java.util.ArrayList;

/**
 * A DataHandler specifically for PatchNotes.
 * extends the generic DataHandler class.
 *
 * @author Alyssa Heimlicher
 * @version 1.0
 * @since 2022-05-23
 */
public class PatchnoteDataHandler extends DataHandlerGen<PatchNote> {

    /**
     * Constructor for the PatchnoteDataHandler class.
     *
     * @author Alyssa Heimlicher
     * @since 1.0
     */
    public PatchnoteDataHandler() {
        super(PatchNote.class);
    }

    /**
     * gets a patch note from the json file by its id.
     *
     * @param uuid the id of the patch note to get.
     * @return the patch note with the given id.
     * @throws IOException            if the json file cannot be read.
     * @throws NoSuchFieldException   if the JSON file does not contain given field
     * @throws IllegalAccessException if the field is not accessible
     * @author Alyssa Heimlicher
     */
    public PatchNote readPatchNoteByUUID(String uuid) throws IOException, NoSuchFieldException, IllegalAccessException {
        return super.getSingleFromJsonArray("patchNoteJSON", "patchNoteUUID", uuid);
    }

    /**
     * gets a list of patch notes from the json file.
     *
     * @return an array list of patch notes.
     * @throws IOException if the json file cannot be read.
     * @author Alyssa Heimlicher
     */
    public ArrayList<PatchNote> getArrayListOutOfJSON() throws IOException {
        return super.getArrayListOutOfJSON("patchNoteJSON");
    }
}
