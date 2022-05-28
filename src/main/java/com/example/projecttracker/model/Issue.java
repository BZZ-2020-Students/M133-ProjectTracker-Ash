package com.example.projecttracker.model;

import lombok.*;

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
    private String issueUUID;
    /**
     * The name of the issue.
     *
     * @since 1.0
     */
    private String title;
    /**
     * The description of the issue.
     *
     * @since 1.0
     */
    private String description;
    /**
     * The severity of the issue.
     *
     * @since 1.0
     */
    private String severity;
    /**
     * The status of the issue. (In progress, Completed, etc.)
     *
     * @since 1.0
     */
    private Status status;
}
