package com.example.projecttracker.data;

import com.example.projecttracker.model.Task;

import java.io.IOException;
import java.util.ArrayList;

public class TaskDataHandler extends DataHandlerGen<Task> {

    public TaskDataHandler() {
        super(Task.class);
    }

    /**
     * reads a Task by its id
     *
     * @param id the id of the Task
     * @return the Task (null=not found)
     */
    public Task readTaskById(int id) throws IOException, NoSuchFieldException, IllegalAccessException {
        return super.getSingleFromJsonArray("taskJSON", "taskId", id);
    }

    public ArrayList<Task> getArrayListOutOfJSON() throws IOException {
        return super.getArrayListOutOfJSON("taskJSON");
    }
}
