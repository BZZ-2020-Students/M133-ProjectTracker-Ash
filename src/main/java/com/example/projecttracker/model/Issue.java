package com.example.projecttracker.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Issue {
    private Integer issueId;
    private String title;
    private String description;
    private String severity;
    private Status status;

    public Issue() {
    }

    public Issue(Integer issueId, String title, String description, String severity, Status status) {
        this.issueId = issueId;
        this.title = title;
        this.description = description;
        this.severity = severity;
    }

}
