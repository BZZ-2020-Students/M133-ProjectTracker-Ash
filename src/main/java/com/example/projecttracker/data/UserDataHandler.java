package com.example.projecttracker.data;

import com.example.projecttracker.model.User;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;

import java.io.IOException;

/**
 * A DataHandler specifically for Users.
 * extends the generic DataHandler class.
 *
 * @author Alyssa Heimlicher
 * @version 1.0
 * @since 2022-05-23
 */
public class UserDataHandler extends DataHandlerGen<User> {

    /**
     * Constructor for UserDataHandler.
     *
     * @author Alyssa Heimlicher
     */
    public UserDataHandler() {
        super(User.class);
    }


    /**
     * reads a User by its uuid
     *
     * @param userUUID the uuid of the user
     * @return the User (null=not found)
     * @throws IOException            if there is an error reading the file
     * @throws NoSuchFieldException   if the field does not exist
     * @throws IllegalAccessException if the field is not accessible
     * @author Alyssa Heimlicher
     */
    public User readUserByUserUUID(String userUUID) throws IOException, NoSuchFieldException, IllegalAccessException {
        return super.getSingleFromJsonArray("userJSON", "userUUID", userUUID);
    }

    /**
     * gets a specific user by their username and password
     *
     * @param username the username of the user
     * @return the user (null=not found)
     * @throws IOException if there is an error reading the file
     *
     * @author Alyssa Heimlicher
     */
    public User readUserByUsername(String username) throws IOException {
        for (User u : getArrayListOutOfJSON("userJSON")) {
            if (u.getUserName().equals(username)) {
                return u;
            }
        }
        return null;
    }

    @Override
    protected FilterProvider getFilterProvider() {
        return new SimpleFilterProvider()
                .addFilter("UserFilter", SimpleBeanPropertyFilter.serializeAll());
    }
}
