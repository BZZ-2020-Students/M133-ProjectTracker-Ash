package com.example.projecttracker.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;

/**
 * Project Class for the Project Tracker App
 * @author Alyssa Heimlicher
 * @version 1.0
 * @since 2020-05-20
 */

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Project {
    /**
     * The project's unique ID
     * @since 1.0
     */
    private Integer projectId;
    /**
     * The project's name
     * @since 1.0
     */
    private String title;
    /**
     * The project's description
     * @since 1.0
     */
    private String description;
    /**
     * The project's start date
     * @since 1.0
     */
    private String startDate;
    /**
     * Boolean value for whether the project is completed
     * @since 1.0
     */
    private Boolean isFinished;
    /**
     * The project's subject (e.g. CS, Math, etc.)
     * @since 1.0
     */
    private String subject;
    /**
     * The User the project belongs to
     * @since 1.0
     */
    private User user;
    /**
     * The list of issues associated with the project
     * @since 1.0
     */
    private ArrayList<Issue> issues;
    /**
     * The list of tasks associated with the project
     * @since 1.0
     */
    private ArrayList<Task> tasks;
    /**
     * The list of patchNotes associated with the project
     * @since 1.0
     */
    private ArrayList<PatchNote> patchNotes;

}