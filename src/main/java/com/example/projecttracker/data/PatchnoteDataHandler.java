package com.example.projecttracker.data;

import com.example.projecttracker.model.PatchNote;

import java.io.IOException;
import java.util.ArrayList;

public class PatchnoteDataHandler extends DataHandlerGen<PatchNote> {
    public PatchnoteDataHandler() {
        super(PatchNote.class);
    }

    public PatchNote readPatchNoteByID(int id) throws IOException, NoSuchFieldException, IllegalAccessException {
        return super.getSingleFromJsonArray("patchNoteJSON", "patchNoteId", id);
    }

    public ArrayList<PatchNote> getArrayListOutOfJSON() throws IOException {
        return super.getArrayListOutOfJSON("patchNoteJSON");
    }
}
