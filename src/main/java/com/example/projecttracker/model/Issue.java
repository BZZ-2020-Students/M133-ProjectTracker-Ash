package com.example.projecttracker.model;

import com.example.projecttracker.util.annotation.IssueSeverity;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import jakarta.ws.rs.FormParam;
import lombok.*;

import java.util.UUID;

import static com.example.projecttracker.util.Constants.*;

/**
 * Issues that can be assigned to a project.
 * Similar to Tasks
 *
 * @author Alyssa Heimlicher
 * @version 1.0
 * @since 2022-05-20
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Issue {
    /**
     * The unique identifier for the issue.
     *
     * @since 1.0
     */
    private String issueUUID = UUID.randomUUID().toString();

    /**
     * The name of the issue.
     *
     * @since 1.0
     */
    @FormParam("title")
    @NotEmpty(message = "Title cannot be empty")
    @Size(min = MIN_TITLE_LENGTH, max = MAX_TITLE_LENGTH)
    private String title;

    /**
     * The description of the issue.
     *
     * @since 1.0
     */
    @FormParam("description")
    @Size(max = MAX_DESCRIPTION_LENGTH)
    private String description;

    /**
     * The severity of the issue.
     *
     * @since 1.0
     */
    @FormParam("severity")
    @IssueSeverity
    @NotEmpty(message = "Severity cannot be empty")
    private String severity;

    /**
     * The status of the issue. (In progress, Completed, etc.)
     *
     * @since 1.0
     */
    @FormParam("status")
    private Status status = Status.TODO;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Issue issue = (Issue) o;
        return issueUUID.equals(issue.issueUUID) && title.equals(issue.title) && description.equals(issue.description) && severity.equals(issue.severity) && status == issue.status;
    }
}
