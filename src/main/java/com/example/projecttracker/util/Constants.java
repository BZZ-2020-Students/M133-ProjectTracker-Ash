package com.example.projecttracker.util;

/**
 * This is a Constants class that contains all the constants used in the project.
 * for easy modification.
 *
 * @author Alyssa Heimlicher
 * @version 1.2
 * @since 2022-06-20
 */
public class Constants {
    /**
     * Max length of every title
     *
     * @since 1.2
     */
    public static final int MAX_TITLE_LENGTH = 64;

    /**
     * Min length of every title
     *
     * @since 1.2
     */
    public static final int MIN_TITLE_LENGTH = 3;

    /**
     * Max length of every description
     *
     * @since 1.2
     */
    public static final int MAX_DESCRIPTION_LENGTH = 1024;

    /**
     * Max length of the subjects
     *
     * @since 1.2
     */
    public static final int MAX_SUBJECT_LENGTH = 24;

    /**
     * Min length of the subjects
     *
     * @since 1.2
     */
    public static final int MIN_SUBJECT_LENGTH = 3;

    /**
     * Max length of the version number
     *
     * @since 1.2
     */
    public static final int MAX_VERSION_LENGTH = 16;

    /**
     * All valid severities for an issue
     *
     * @since 1.2
     */
    public static final String[] VALID_SEVERITIES = {"critical", "major", "minor", "trivial"};

    /**
     * Max length for a username
     *
     * @since 1.2
     */
    public static final int MAX_USERNAME_LENGTH = 24;

    /**
     * Min length for a username
     *
     * @since 1.2
     */
    public static final int MIN_USERNAME_LENGTH = 3;

    /**
     * Max length for a password
     *
     * @since 1.2
     */
    public static final int MAX_PASSWORD_LENGTH = 16;

    /**
     * Min length for a password
     *
     * @since 1.2
     */
    public static final int MIN_PASSWORD_LENGTH = 8;


}