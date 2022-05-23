package com.example.projecttracker.data;

import com.example.projecttracker.model.Issue;
import com.example.projecttracker.model.PatchNote;
import com.example.projecttracker.model.Project;
import com.example.projecttracker.model.Task;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Utility class for handling data regarding Projects in JSON files.
 *
 * @author Alyssa Heimlicher
 * @version 1.0
 * @see Project
 * @since 2022-05-23
 */
public class ProjectDatahandler extends DataHandlerGen<Project> {
    /**
     * Default constructor.
     *
     * @author Alyssa Heimlicher
     * @since 2020-05-23
     */
    public ProjectDatahandler() {
        super(Project.class);
    }

    /**
     * Gets a single project from the JSON file.
     *
     * @param fieldValue the value of the field to search for
     * @return the project with the given field value or null if no project with that field value exists
     * @throws IOException            if the file cannot be read
     * @throws NoSuchFieldException   if the field does not exist
     * @throws IllegalAccessException if the field cannot be accessed
     * @author Alyssa Heimlicher
     * @since 2020-05-23
     */
    public Project getSingleFromJsonArray(Object fieldValue) throws IOException, NoSuchFieldException, IllegalAccessException {
        Project project = super.getSingleFromJsonArray("projectJSON", "projectId", fieldValue);

        if (project != null) {
            ArrayList<Task> tasks = new ArrayList<>();
            TaskDataHandler taskDataHandler = new TaskDataHandler();
            setTasks(tasks, taskDataHandler, project);

            ArrayList<Issue> issues = new ArrayList<>();
            IssueDataHandler issueDataHandler = new IssueDataHandler();
            setIssues(issues, issueDataHandler, project);

            ArrayList<PatchNote> patchNotes = new ArrayList<>();
            PatchnoteDataHandler patchnoteDataHandler = new PatchnoteDataHandler();
            setPatchNotes(patchNotes, patchnoteDataHandler, project);
        }

        return project;
    }

    public ArrayList<Project> getArrayListOutOfJSON() throws IOException, NoSuchFieldException, IllegalAccessException {
        ArrayList<Project> projects = super.getArrayListOutOfJSON("projectJSON");

        ArrayList<Task> tasks = new ArrayList<>();
        ArrayList<Issue> issues = new ArrayList<>();
        ArrayList<PatchNote> patchNotes = new ArrayList<>();
        TaskDataHandler taskDataHandler = new TaskDataHandler();
        IssueDataHandler issueDataHandler = new IssueDataHandler();
        PatchnoteDataHandler patchnoteDataHandler = new PatchnoteDataHandler();
        for (Project project : projects) {
            setTasks(tasks, taskDataHandler, project);
            setIssues(issues, issueDataHandler, project);
            setPatchNotes(patchNotes, patchnoteDataHandler, project);
        }

        return projects;
    }

    private void setTasks(ArrayList<Task> tasks, TaskDataHandler taskDataHandler, Project project) throws IOException, NoSuchFieldException, IllegalAccessException {
        for (Integer taskID : project.getTaskIds()) {
            Task task = taskDataHandler.readTaskById(taskID);
            tasks.add(task);
        }
        project.setTasks(tasks);
    }

    private void setIssues(ArrayList<Issue> issues, IssueDataHandler issueDataHandler, Project project) throws IOException, NoSuchFieldException, IllegalAccessException {
        for (Integer issueId : project.getIssueIds()) {
            Issue issue = issueDataHandler.readIssueByID(issueId);
            issues.add(issue);
        }
        project.setIssues(issues);
    }

    private void setPatchNotes(ArrayList<PatchNote> patchNotes, PatchnoteDataHandler patchnoteDataHandler, Project project) throws IOException, NoSuchFieldException, IllegalAccessException {
        for (Integer patchnoteID : project.getTaskIds()) {
            PatchNote patchNote = patchnoteDataHandler.readPatchNoteByID(patchnoteID);
            patchNotes.add(patchNote);
        }
        project.setPatchNotes(patchNotes);
    }
}
