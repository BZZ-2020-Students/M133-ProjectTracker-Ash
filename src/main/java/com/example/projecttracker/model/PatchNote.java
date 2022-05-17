package com.example.projecttracker.model;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class PatchNote {
    private Integer patchNoteId;
    private String title;
    private String description;
    private Date date;
    private String version;

    public PatchNote() {

    }

    public PatchNote(Integer patchNoteId, String title, String description, Date date, String version) {
        this.patchNoteId = patchNoteId;
        this.title = title;
        this.description = description;
        this.date = date;
        this.version = version;
    }

}
