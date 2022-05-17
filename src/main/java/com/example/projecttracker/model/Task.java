package com.example.projecttracker.model;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class Task {
    private Integer taskId;
    private String title;
    private String description;
    private Date deadline;
    private Status status;

    public Task() {
    }

    public Task(Integer taskId, String title, String description, Date deadline, Status status) {
        this.taskId = taskId;
        this.title = title;
        this.description = description;
        this.deadline = deadline;
        this.status = status;
    }

}
