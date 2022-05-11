package com.example.projecttracker.model;

public class Issue {
    private int issueId;
    private String title;
    private String description;
    private String severity;
    private String status;//TODO: add status enum

    public Issue() {
    }

    public Issue(int issueId, String title, String description, String severity, String status) {
        this.issueId = issueId;
        this.title = title;
        this.description = description;
        this.severity = severity;
    }

    public int getIssueId() {
        return issueId;
    }

    public void setIssueId(int issueId) {
        this.issueId = issueId;
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

    public String getSeverity() {
        return severity;
    }

    public void setSeverity(String severity) {
        this.severity = severity;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
