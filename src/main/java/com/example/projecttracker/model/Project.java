package com.example.projecttracker.model;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

@Getter
@Setter
public class Project {
    private Integer projectId;
    private String title;
    private String description;
    private String startDate;
    private Boolean isFinished;
    private String subject;
    private User user;
    private ArrayList<Issue> issues;
    private ArrayList<Task> tasks;
    private ArrayList<PatchNote> patchNotes;

    public Project() {
    }

    public Project(Integer projectId, String title, String description, String startDate, Boolean isfinished, String subject, User user, ArrayList<Issue> issues, ArrayList<Task> tasks, ArrayList<PatchNote> patchNotes) {
        this.projectId = projectId;
        this.title = title;
        this.description = description;
        this.startDate = startDate;
        this.isFinished = isfinished;
        this.subject = subject;
        this.user = user;
        this.issues = issues;
        this.tasks = tasks;
        this.patchNotes = patchNotes;
    }

}
