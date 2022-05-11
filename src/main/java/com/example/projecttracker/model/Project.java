package com.example.projecttracker.model;

import java.util.ArrayList;

public class Project {
    private int projectId;
    private String title;
    private String description;
    private String startDate;
    private boolean isFinished;
    private String subject;
    private User user;
    private ArrayList<Issue> issues;
    private ArrayList<Task> tasks;
    private ArrayList<PatchNote> patchNotes;

    public Project() {
    }

    public Project(int projectId, String title, String description, String startDate, boolean isfinished, String subject, User user, ArrayList<Issue> issues, ArrayList<Task> tasks, ArrayList<PatchNote> patchNotes) {
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

    public int getProjectId() {
        return projectId;
    }

    public void setProjectId(int projectId) {
        this.projectId = projectId;
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

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public boolean isFinished() {
        return isFinished;
    }

    public void setFinished(boolean finished) {
        isFinished = finished;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public ArrayList<Issue> getIssues() {
        return issues;
    }

    public void setIssues(ArrayList<Issue> issues) {
        this.issues = issues;
    }

    public ArrayList<Task> getTasks() {
        return tasks;
    }

    public void setTasks(ArrayList<Task> tasks) {
        this.tasks = tasks;
    }

    public ArrayList<PatchNote> getPatchNotes() {
        return patchNotes;
    }

    public void setPatchNotes(ArrayList<PatchNote> patchNotes) {
        this.patchNotes = patchNotes;
    }
}
