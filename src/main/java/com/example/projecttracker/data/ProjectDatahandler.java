package com.example.projecttracker.data;

import com.example.projecttracker.model.Project;
import com.example.projecttracker.model.Task;

import java.io.IOException;
import java.util.ArrayList;

public class ProjectDatahandler extends DataHandlerGen<Project> {
    public ProjectDatahandler() {
        super(Project.class);
    }

    public Project getSingleFromJsonArray(Object fieldValue) throws IOException, NoSuchFieldException, IllegalAccessException {
        Project project = super.getSingleFromJsonArray("projectJSON", "projectId", fieldValue);

        if (project != null) {
            ArrayList<Task> tasks = new ArrayList<>();
            TaskDataHandler taskDataHandler = new TaskDataHandler();
            setTasks(tasks, taskDataHandler, project);
        }

        return project;
    }

    public ArrayList<Project> getArrayListOutOfJSON() throws IOException, NoSuchFieldException, IllegalAccessException {
        ArrayList<Project> projects = super.getArrayListOutOfJSON("projectJSON");

        ArrayList<Task> tasks = new ArrayList<>();
        TaskDataHandler taskDataHandler = new TaskDataHandler();
        for (Project project : projects) {
            setTasks(tasks, taskDataHandler, project);
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
}
