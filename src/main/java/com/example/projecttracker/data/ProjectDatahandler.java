package com.example.projecttracker.data;

import com.example.projecttracker.model.Issue;
import com.example.projecttracker.model.PatchNote;
import com.example.projecttracker.model.Project;
import com.example.projecttracker.model.Task;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
     * Gets a single project from the JSON file. If project is found, we also set the tasks, issues, and patch notes.
     * if project is not found, we return null.
     *
     * @param fieldValue the value of the field to search for
     * @return the project with the given field value or null if no project with that field value exists
     * @throws IOException            if the file cannot be read
     * @throws NoSuchFieldException   if the field does not exist
     * @throws IllegalAccessException if the field cannot be accessed
     * @author Alyssa Heimlicher
     * @see DataHandlerGen#getSingleFromJsonArray(String, String, Object)
     * @since 2020-05-23
     */
    public Project getSingleFromJsonArray(Object fieldValue) throws IOException, NoSuchFieldException, IllegalAccessException {
        Project project = super.getSingleFromJsonArray("projectJSON", "projectUUID", fieldValue);

        if (project != null) {
            TaskDataHandler taskDataHandler = new TaskDataHandler();
            setTasks(taskDataHandler, project);

            IssueDataHandler issueDataHandler = new IssueDataHandler();
            setIssues(issueDataHandler, project);

            PatchnoteDataHandler patchnoteDataHandler = new PatchnoteDataHandler();
            setPatchNotes(patchnoteDataHandler, project);
        }

        return project;
    }

    /**
     * Returns all found Projects. Also sets the tasks, issues, and patch notes of each project.
     *
     * @return all found Projects
     * @throws IOException            if the file cannot be read
     * @throws NoSuchFieldException   if the field does not exist
     * @throws IllegalAccessException if the field cannot be accessed
     * @author Alyssa Heimlicher
     * @see DataHandlerGen#getArrayListOutOfJSON(String)
     * @since 2020-05-23
     */
    public ArrayList<Project> getArrayListOutOfJSON() throws IOException, NoSuchFieldException, IllegalAccessException {
        ArrayList<Project> projects = super.getArrayListOutOfJSON("projectJSON");

        TaskDataHandler taskDataHandler = new TaskDataHandler();
        IssueDataHandler issueDataHandler = new IssueDataHandler();
        PatchnoteDataHandler patchnoteDataHandler = new PatchnoteDataHandler();
        for (Project project : projects) {
            setTasks(taskDataHandler, project);
            setIssues(issueDataHandler, project);
            setPatchNotes(patchnoteDataHandler, project);
        }

        return projects;
    }

    public void deleteSingleFromJson(String uuid) throws IOException, NoSuchFieldException, IllegalAccessException {
        Project project = new ProjectDatahandler().getSingleFromJsonArray(uuid);
        ArrayList<Project> projects = new ProjectDatahandler().getArrayListOutOfJSON();
        List<Task> tasks = project.getTasks();
        List<Issue> issues = project.getIssues();
        List<PatchNote> patchNotes = project.getPatchNotes();
        for (Task task : tasks) {
            new TaskDataHandler().deleteSingleFromJson("taskJSON", "taskUUID", task.getTaskUUID());
        }
        for (Issue issue : issues) {
            new IssueDataHandler().deleteSingleFromJson("issueJSON", "issueUUID", issue.getIssueUUID());
        }
        for (PatchNote patchNote : patchNotes) {
            new PatchnoteDataHandler().deleteSingleFromJson("patchNoteJSON", "patchNoteUUID", patchNote.getPatchNoteUUID());
        }
        projects.remove(project);
        project.removeAllTasks();
        project.removeAllIssues();
        project.removeAllPatchNotes();
        projects.add(project);
        new ProjectDatahandler().saveJson("projectJSON", projects);
        super.deleteSingleFromJson("projectJSON", "projectUUID", uuid);
    }

    /**
     * Utility method to set the tasks of a project.
     *
     * @param taskDataHandler The data handler to use to get the tasks
     * @param project         The project to set the tasks of
     * @throws IOException            if the file cannot be read
     * @throws NoSuchFieldException   if the field does not exist
     * @throws IllegalAccessException if the field cannot be accessed
     * @author Alyssa Heimlicher
     * @since 2020-05-23
     */
    private void setTasks(TaskDataHandler taskDataHandler, Project project) throws IOException, NoSuchFieldException, IllegalAccessException {
        ArrayList<Task> tasks = new ArrayList<>();
        for (String taskUUid : project.getTaskUUIds()) {
            Task task = taskDataHandler.readTaskByUUID(taskUUid);
            tasks.add(task);
        }
        project.setTasks(tasks);
    }

    /**
     * Utility method to set the issues of a project.
     *
     * @param issueDataHandler The data handler to use to get the issues
     * @param project          The project to set the issues of
     * @throws IOException            if the file cannot be read
     * @throws NoSuchFieldException   if the field does not exist
     * @throws IllegalAccessException if the field cannot be accessed
     * @author Alyssa Heimlicher
     * @since 2020-05-23
     */
    private void setIssues(IssueDataHandler issueDataHandler, Project project) throws IOException, NoSuchFieldException, IllegalAccessException {
        ArrayList<Issue> issues = new ArrayList<>();
        for (String issueUUID : project.getIssueUUID()) {
            Issue issue = issueDataHandler.readIssueByUUID(issueUUID);
            issues.add(issue);
        }
        project.setIssues(issues);
    }

    /**
     * Utility method to set the patch notes of a project.
     *
     * @param patchnoteDataHandler The data handler to use to get the patch notes
     * @param project              The project to set the patch notes of
     * @throws IOException            if the file cannot be read
     * @throws NoSuchFieldException   if the field does not exist
     * @throws IllegalAccessException if the field cannot be accessed
     * @author Alyssa Heimlicher
     * @since 2020-05-23
     */
    private void setPatchNotes(PatchnoteDataHandler patchnoteDataHandler, Project project) throws IOException, NoSuchFieldException, IllegalAccessException {
        ArrayList<PatchNote> patchNotes = new ArrayList<>();
        for (String patchnoteUUID : project.getPatchnoteUUIDs()) {
            PatchNote patchNote = patchnoteDataHandler.readPatchNoteByUUID(patchnoteUUID);
            patchNotes.add(patchNote);
        }
        project.setPatchNotes(patchNotes);
    }
}
