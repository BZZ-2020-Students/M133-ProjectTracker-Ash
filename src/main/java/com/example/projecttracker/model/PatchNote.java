package com.example.projecttracker.model;

import java.util.Date;

public class PatchNote {
    private int patchNoteId;
    private String title;
    private String description;
    private Date date;
    private String version;

    public PatchNote() {

    }

    public PatchNote(int patchNoteId, String title, String description, Date date, String version) {
        this.patchNoteId = patchNoteId;
        this.title = title;
        this.description = description;
        this.date = date;
        this.version = version;
    }

    public int getPatchNoteId() {
        return patchNoteId;
    }

    public void setPatchNoteId(int patchNoteId) {
        this.patchNoteId = patchNoteId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }
}
