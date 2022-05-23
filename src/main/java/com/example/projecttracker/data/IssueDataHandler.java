package com.example.projecttracker.data;

import com.example.projecttracker.model.Issue;

import java.io.IOException;
import java.util.ArrayList;

public class IssueDataHandler extends DataHandlerGen<Issue> {
    public IssueDataHandler() {
        super(Issue.class);
    }

    public Issue readIssueByID(int id) throws IOException, NoSuchFieldException, IllegalAccessException {
        return super.getSingleFromJsonArray("issueJSON", "issueId", id);
    }

    public ArrayList<Issue> getArrayListOutOfJSON() throws IOException {
        return super.getArrayListOutOfJSON("issueJSON");
    }

}
