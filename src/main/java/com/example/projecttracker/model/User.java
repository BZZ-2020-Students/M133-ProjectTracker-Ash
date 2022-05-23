package com.example.projecttracker.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * The User class
 *
 * @author Alyssa Heimlicher
 * @version 1.0
 * @since 2022-05-20
 */

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {
    /**
     * The user's unique uuid
     *
     * @since 1.0
     */
    private String userUUID;
    /**
     * The user's username
     *
     * @since 1.0
     */
    private String userName;
    /**
     * The user's password
     *
     * @since 1.0
     */
    private String password;
    /**
     * The Users role in the application
     *
     * @since 1.0
     */
    private String userRole;


}
