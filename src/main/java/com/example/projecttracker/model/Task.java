package com.example.projecttracker.model;

import lombok.*;

import java.util.Date;
import java.util.Objects;
import java.util.UUID;

/**
 * Tasks that can be assigned to a project.
 *
 * @author Alyssa Heimlicher
 * @version 1.0
 * @since 2022-05-20
 */

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Task {
    /**
     * The id of the task.
     *
     * @since 1.0
     */
    private String taskUUID;
    /**
     * The name of the task.
     *
     * @since 1.0
     */
    private String title;
    /**
     * The description of the task.
     *
     * @since 1.0
     */
    private String description;
    /**
     * The date of when the task is due.
     *
     * @since 1.0
     */
    private Date deadline;
    /**
     * Status of the task. (In progress, Completed, etc.)
     *
     * @since 1.0
     */
    private Status status;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return taskUUID.equals(task.taskUUID) && title.equals(task.title) && description.equals(task.description) && deadline.equals(task.deadline) && status == task.status;
    }
}
