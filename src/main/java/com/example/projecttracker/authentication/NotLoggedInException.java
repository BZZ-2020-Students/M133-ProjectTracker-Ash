package com.example.projecttracker.authentication;

public class NotLoggedInException extends Exception {
    /**
     * Constructs a new exception with the specified detail message.
     *
     * @author Zwazel
     * @since 1.3
     */
    public NotLoggedInException() {
        super("User is not logged in");
    }
}