package com.example.projecttracker.data;

import com.example.projecttracker.model.Task;

import java.io.IOException;
import java.util.ArrayList;

/**
 * A DataHandler specifically for tasks.
 * extends the generic DataHandler class.
 *
 * @author Alyssa Heimlicher
 * @version 1.0
 * @since 2022-05-23
 */
public class TaskDataHandler extends DataHandlerGen<Task> {

    /**
     * Constructor for TaskDataHandler.
     *
     * @author Alyssa Heimlicher
     */
    public TaskDataHandler() {
        super(Task.class);
    }

    /**
     * reads a Task by its id
     *
     * @param uuid the uuid of the Task
     * @return the Task (null=not found)
     * @throws IOException if the file cannot be read
     * @throws NoSuchFieldException if the field is not found
     * @throws IllegalAccessException if the field is not accessible
     * @author Alyssa Heimlicher
     */
    public Task readTaskByUUID(String uuid) throws IOException, NoSuchFieldException, IllegalAccessException {
        return super.getSingleFromJsonArray("taskJSON", "taskUUID", uuid);
    }

    /**
     * reads all the tasks in the json file
     *
     * @return an ArrayList of all the tasks
     * @throws IOException if the file cannot be read
     * @author Alyssa Heimlicher
     */
    public ArrayList<Task> getArrayListOutOfJSON() throws IOException {
        return super.getArrayListOutOfJSON("taskJSON");
    }


}
