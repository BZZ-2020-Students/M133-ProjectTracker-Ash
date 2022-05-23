package com.example.projecttracker.data;

import com.example.projecttracker.model.User;

import java.io.IOException;

public class UserDataHandler extends DataHandlerGen<User> {

    public UserDataHandler() {
        super(User.class);
    }


    /**
     * reads a User by its uuid
     *
     * @param userUUID the uuid of the user
     * @return the User (null=not found)
     */
    public User readUserByUserUUID(String userUUID) throws IOException, NoSuchFieldException, IllegalAccessException {
        return super.getSingleFromJsonArray("userJSON", "userUUID", userUUID);
    }
}
