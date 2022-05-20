package com.example.projecttracker.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Task {
    /**
     * The id of the task.
     * @since 1.0
     */
    private Integer taskId;
    /**
     * The name of the task.
     * @since 1.0
     */
    private String title;
    /**
     * The description of the task.
     * @since 1.0
     */
    private String description;
    /**
     * The date of when the task is due.
     * @since 1.0
     */
    private Date deadline;
    /**
     * Status of the task.
     * @since 1.0
     */
    private Status status;

}
