package com.example.projecttracker.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import jakarta.ws.rs.FormParam;
import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

import static com.example.projecttracker.util.Constants.*;

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
@JsonIgnoreProperties({"tempDate"})
@Builder
public class Task {
    /**
     * The id of the task.
     *
     * @since 1.0
     */
    private String taskUUID = UUID.randomUUID().toString();
    /**
     * The name of the task.
     *
     * @since 1.0
     */
    @FormParam("title")
    @NotEmpty(message = "Title cannot be empty")
    @Size(min=MIN_TITLE_LENGTH, max=MAX_TITLE_LENGTH)
    private String title;
    /**
     * The description of the task.
     *
     * @since 1.0
     */
    @FormParam("description")
    @Size(max=MAX_DESCRIPTION_LENGTH)
    private String description;
    /**
     * The date of when the task is due.
     *
     * @since 1.0
     */
    private LocalDate deadline;

    @FormParam("deadline")
    @NotEmpty(message = "Deadline cannot be empty")
    private String tempDate;

    /**
     * Status of the task. (In progress, Completed, etc.)
     *
     * @since 1.0
     */
    private Status status = Status.TODO;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return taskUUID.equals(task.taskUUID) && title.equals(task.title) && description.equals(task.description) && deadline.equals(task.deadline) && status == task.status;
    }
}
