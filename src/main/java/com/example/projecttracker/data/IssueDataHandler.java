package com.example.projecttracker.data;

import com.example.projecttracker.model.Issue;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Separate DataHandler specifically for the Issue class.
 * extends my generic DataHandler class.
 *
 * @author Alyssa Heimlicher
 * @version 1.0
 * @since 2022-05-23
 */
public class IssueDataHandler extends DataHandlerGen<Issue> {
    /**
     * Constructor for the IssueDataHandler class.
     *
     * @author Alyssa Heimlicher
     * @since 1.0
     */
    public IssueDataHandler() {
        super(Issue.class);
    }

    /**
     * Reads a specific issue from the JSON file with the given ID.
     *
     * @param id the ID of the issue to be read.
     * @return the issue with the given ID.
     * @throws IOException            if the JSON file cannot be read.
     * @throws NoSuchFieldException   if the JSON file does not contain given field
     * @throws IllegalAccessException If the field is not accessible
     * @author Alyssa Heimlicher
     */
    public Issue readIssueByID(int id) throws IOException, NoSuchFieldException, IllegalAccessException {
        return super.getSingleFromJsonArray("issueJSON", "issueId", id);
    }

    /**
     * Gets all issues from the JSON file.
     *
     * @return an ArrayList of all issues in the JSON file.
     * @throws IOException if the JSON file cannot be read.
     * @author Alyssa Heimlicher
     */
    public ArrayList<Issue> getArrayListOutOfJSON() throws IOException {
        return super.getArrayListOutOfJSON("issueJSON");
    }

}
