package com.example.projecttracker.model;

import com.fasterxml.jackson.annotation.JsonFilter;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

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
@JsonFilter("UserFilter")
public class User {
    /**
     * The user's unique uuid
     *
     * @since 1.0
     */
    private String userUUID = UUID.randomUUID().toString();
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


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return userUUID.equals(user.userUUID) && userName.equals(user.userName) && password.equals(user.password) && userRole.equals(user.userRole);
    }
}
