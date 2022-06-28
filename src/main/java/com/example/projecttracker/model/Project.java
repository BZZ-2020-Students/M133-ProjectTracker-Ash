package com.example.projecttracker.model;

import com.example.projecttracker.data.IssueDataHandler;
import com.example.projecttracker.data.PatchnoteDataHandler;
import com.example.projecttracker.data.TaskDataHandler;
import com.example.projecttracker.data.UserDataHandler;
import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import jakarta.ws.rs.FormParam;
import lombok.*;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.UUID;

import static com.example.projecttracker.util.Constants.*;

/**
 * Project Class for the Project Tracker App
 *
 * @author Alyssa Heimlicher
 * @version 1.0
 * @since 2022-05-20
 */

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@JsonFilter("ProjectFilter")
@JsonIgnoreProperties({"tempStartDate", "userid"})
public class Project {
    /**
     * The project's unique ID
     */
    private String projectUUID = UUID.randomUUID().toString();
    /**
     * The project's name
     */
    @FormParam("title")
    @NotEmpty(message = "Title cannot be empty")
    @Size(min = MIN_TITLE_LENGTH, max = MAX_TITLE_LENGTH)
    private String title;

    /**
     * The project's description
     */
    @FormParam("description")
    @Size(max = MAX_DESCRIPTION_LENGTH)
    private String description;
    /**
     * The project's start date
     */
    private LocalDate startDate;


    /**
     * A temporary start date used for the project's start date
     *
     */
    @FormParam("startDate")
    @NotEmpty(message = "Start date cannot be empty")
    private String tempStartDate;

    /**
     * Boolean value for whether the project is completed
     */
    private Boolean isFinished = false;
    /**
     * The project's subject (e.g. CS, Math, etc.)
     */
    @FormParam("subject")
    @NotEmpty(message = "Subject cannot be empty")
    @Size(min = MIN_SUBJECT_LENGTH, max = MAX_SUBJECT_LENGTH)
    private String subject;

    /**
     * The User the project belongs to
     *
     * @since 1.0
     */
    private User user;

    /**
     * A temp variable for the user's ID
     *
     * @since 1.2
     */
    @FormParam("userUUID")
    @NotEmpty(message = "User cannot be empty")
    private String userid;

    /**
     * The list of issues associated with the project
     */
    private ArrayList<Issue> issues = new ArrayList<>();
    /**
     * The list of tasks associated with the project
     */
    private ArrayList<Task> tasks = new ArrayList<>();
    /**
     * The list of patchNotes associated with the project
     */
    private ArrayList<PatchNote> patchNotes = new ArrayList<>();

    /**
     * gets the userUUID from the User-object
     *
     * @return userUUID
     * @author Alyssa Heimlicher
     */
    public String getUserUUID() {
        return getUser().getUserUUID();
    }

    /**
     * creates a User-Object without the project
     *
     * @param userUUID the userUUID of the user
     * @throws IOException            if the json file cannot be read
     * @throws NoSuchFieldException   if the field does not exist
     * @throws IllegalAccessException if the field cannot be accessed
     * @author Alyssa Heimlicher
     */
    public void setUserUUID(String userUUID) throws IOException, NoSuchFieldException, IllegalAccessException {
        setUser(new User());
        User user = new UserDataHandler().readUserByUserUUID(userUUID);
        if (user != null) {
            getUser().setUserUUID(userUUID);
            getUser().setUserName(user.getUserName());
            getUser().setPassword(user.getPassword());
            getUser().setUserRole(user.getUserRole());
        }
    }

    /**
     * get the taskIds from the tasks
     *
     * @return an ArrayList of taskIds
     * @author Alyssa Heimlicher
     */
    public ArrayList<String> getTaskUUIDs() {
        ArrayList<String> taskUUIDs = new ArrayList<>();
        for (Task task : getTasks()) {
            if (task != null) {
                taskUUIDs.add(task.getTaskUUID());
            }
        }
        return taskUUIDs;
    }

    /**
     * set the taskIds from the tasks to the project
     *
     * @param taskUUIDs the taskUUIDs to set
     * @throws IOException            if the json file cannot be read
     * @throws NoSuchFieldException   if the field does not exist
     * @throws IllegalAccessException if the file cannot be accessed
     * @author Alyssa Heimlicher
     */
    public void setTaskUUIDs(ArrayList<String> taskUUIDs) throws IOException, NoSuchFieldException, IllegalAccessException {
        TaskDataHandler taskDataHandler = new TaskDataHandler();
        for (String taskUUID : taskUUIDs) {
            getTasks().add(taskDataHandler.readTaskByUUID(taskUUID));
        }
    }

    /**
     * get the issueIds from the issues in the project
     *
     * @return an ArrayList of issueIds
     * @author Alyssa Heimlicher
     */
    public ArrayList<String> getIssueUUIDs() {
        ArrayList<String> issueIds = new ArrayList<>();
        for (Issue issue : getIssues()) {
            if (issue != null) {
                issueIds.add(issue.getIssueUUID());
            }
        }
        return issueIds;
    }

    /**
     * set the issueIds from the issues to the project
     *
     * @param issueUUIDs the issueUUID to set
     * @throws IOException            if the json file cannot be read
     * @throws NoSuchFieldException   if the field does not exist
     * @throws IllegalAccessException if the file cannot be accessed
     * @author Alyssa Heimlicher
     */
    public void setIssueUUIDs(ArrayList<String> issueUUIDs) throws IOException, NoSuchFieldException, IllegalAccessException {
        IssueDataHandler issueDataHandler = new IssueDataHandler();
        for (String issueUUID : issueUUIDs) {
            getIssues().add(issueDataHandler.readIssueByUUID(issueUUID));
        }
    }

    /**
     * get the patchNoteIds from the patchNotes in the project
     *
     * @return an ArrayList of patchNoteIds
     * @author Alyssa Heimlicher
     */
    public ArrayList<String> getPatchNoteUUIDs() {
        ArrayList<String> patchnoteUUIDs = new ArrayList<>();
        for (PatchNote patchnote : getPatchNotes()) {
            if (patchnote != null) {
                patchnoteUUIDs.add(patchnote.getPatchNoteUUID());
            }
        }
        return patchnoteUUIDs;
    }

    /**
     * set the patchNoteIds from the patchNotes to the project
     *
     * @param patchnoteUUIDs the patchNoteUUIDs to set
     * @throws IOException            if the json file cannot be read
     * @throws NoSuchFieldException   if the field does not exist
     * @throws IllegalAccessException if the file cannot be accessed
     * @author Alyssa Heimlicher
     */
    public void setPatchNoteUUIDs(ArrayList<String> patchnoteUUIDs) throws IOException, NoSuchFieldException, IllegalAccessException {
        PatchnoteDataHandler patchnoteDataHandler = new PatchnoteDataHandler();
        for (String patchnoteUUID : patchnoteUUIDs) {
            getPatchNotes().add(patchnoteDataHandler.readPatchNoteByUUID(patchnoteUUID));
        }
    }

    /**
     * removes all the tasks from the project
     *
     * @author Alyssa Heimlicher
     */
    public void removeAllTasks() {
        getTasks().clear();
    }

    /**
     * removes all the issues from the project
     *
     * @author Alyssa Heimlicher
     */
    public void removeAllIssues() {
        getIssues().clear();
    }

    /**
     * removes all the patchNotes from the project
     *
     * @author Alyssa Heimlicher
     */
    public void removeAllPatchNotes() {
        getPatchNotes().clear();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Project project = (Project) o;
        return projectUUID.equals(project.projectUUID) && title.equals(project.title) && description.equals(project.description) && startDate.equals(project.startDate) && isFinished.equals(project.isFinished) && subject.equals(project.subject) && user.equals(project.user) && issues.equals(project.issues) && tasks.equals(project.tasks) && patchNotes.equals(project.patchNotes);
    }
}