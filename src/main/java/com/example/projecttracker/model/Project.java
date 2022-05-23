package com.example.projecttracker.model;

import com.example.projecttracker.data.IssueDataHandler;
import com.example.projecttracker.data.PatchnoteDataHandler;
import com.example.projecttracker.data.TaskDataHandler;
import com.example.projecttracker.data.UserDataHandler;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.IOException;
import java.util.ArrayList;

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
public class Project {
    /**
     * The project's unique ID
     */
    private Integer projectId;
    /**
     * The project's name
     */
    private String title;
    /**
     * The project's description
     */
    private String description;
    /**
     * The project's start date
     */
    private String startDate;
    /**
     * Boolean value for whether the project is completed
     */
    private Boolean isFinished;
    /**
     * The project's subject (e.g. CS, Math, etc.)
     */
    private String subject;
    /**
     * The User the project belongs to
     */
    private User user;
    /**
     * The list of issues associated with the project
     */
    private ArrayList<Issue> issues;
    /**
     * The list of tasks associated with the project
     */
    private ArrayList<Task> tasks;
    /**
     * The list of patchNotes associated with the project
     */
    private ArrayList<PatchNote> patchNotes;

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
        getUser().setUserUUID(userUUID);
        getUser().setUserName(user.getUserName());
        getUser().setPassword(user.getPassword());
        getUser().setUserRole(user.getUserRole());

    }

    /**
     * get the taskIds from the tasks
     *
     * @return an ArrayList of taskIds
     * @author Alyssa Heimlicher
     */
    public ArrayList<Integer> getTaskIds() {
        ArrayList<Integer> taskIds = new ArrayList<>();
        for (Task task : getTasks()) {
            taskIds.add(task.getTaskId());
        }
        return taskIds;
    }

    /**
     * set the taskIds from the tasks to the project
     *
     * @param taskIds the taskIds to set
     * @throws IOException            if the json file cannot be read
     * @throws NoSuchFieldException   if the field does not exist
     * @throws IllegalAccessException if the file cannot be accessed
     * @author Alyssa Heimlicher
     */
    public void setTaskIds(ArrayList<Integer> taskIds) throws IOException, NoSuchFieldException, IllegalAccessException {
        TaskDataHandler taskDataHandler = new TaskDataHandler();
        for (Integer taskID : taskIds) {
            getTasks().add(taskDataHandler.readTaskById(taskID));
        }
    }

    /**
     * get the issueIds from the issues in the project
     *
     * @return an ArrayList of issueIds
     * @author Alyssa Heimlicher
     */
    public ArrayList<Integer> getIssueIds() {
        ArrayList<Integer> issueIds = new ArrayList<>();
        for (Issue issue : getIssues()) {
            issueIds.add(issue.getIssueId());
        }
        return issueIds;
    }

    /**
     * set the issueIds from the issues to the project
     *
     * @param issueIds the issueIds to set
     * @throws IOException            if the json file cannot be read
     * @throws NoSuchFieldException   if the field does not exist
     * @throws IllegalAccessException if the file cannot be accessed
     * @author Alyssa Heimlicher
     */
    public void setIssueIds(ArrayList<Integer> issueIds) throws IOException, NoSuchFieldException, IllegalAccessException {
        IssueDataHandler issueDataHandler = new IssueDataHandler();
        for (Integer taskID : issueIds) {
            getIssues().add(issueDataHandler.readIssueByID(taskID));
        }
    }

    /**
     * get the patchNoteIds from the patchNotes in the project
     *
     * @return an ArrayList of patchNoteIds
     * @author Alyssa Heimlicher
     */
    public ArrayList<Integer> getPatchnoteIds() {
        ArrayList<Integer> patchnoteIds = new ArrayList<>();
        for (PatchNote patchnote : getPatchNotes()) {
            patchnoteIds.add(patchnote.getPatchNoteId());
        }
        return patchnoteIds;
    }

    /**
     * set the patchNoteIds from the patchNotes to the project
     *
     * @param patchnoteIds the patchNoteIds to set
     * @throws IOException            if the json file cannot be read
     * @throws NoSuchFieldException   if the field does not exist
     * @throws IllegalAccessException if the file cannot be accessed
     * @author Alyssa Heimlicher
     */
    public void setPatchnoteIds(ArrayList<Integer> patchnoteIds) throws IOException, NoSuchFieldException, IllegalAccessException {
        PatchnoteDataHandler patchnoteDataHandler = new PatchnoteDataHandler();
        for (Integer patchnoteId : patchnoteIds) {
            getPatchNotes().add(patchnoteDataHandler.readPatchNoteByID(patchnoteId));
        }
    }
}