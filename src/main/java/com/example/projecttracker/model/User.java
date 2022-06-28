package com.example.projecttracker.model;

import com.fasterxml.jackson.annotation.JsonFilter;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import jakarta.ws.rs.FormParam;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

import static com.example.projecttracker.util.Constants.*;

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
    @FormParam("username")
    @NotEmpty(message = "Username cannot be empty")
    @Size(min = MIN_USERNAME_LENGTH, max = MAX_USERNAME_LENGTH)
    private String userName;
    /**
     * The user's password
     *
     * @since 1.0
     */
    @FormParam("password")
    @NotEmpty(message = "Password cannot be empty")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!-_%*?&]{"+MIN_PASSWORD_LENGTH+","+MAX_PASSWORD_LENGTH+"}$", message = "Password must contain at least one lowercase letter, one uppercase letter, one digit, and one special character, and must be between "+MIN_PASSWORD_LENGTH+" and "+MAX_PASSWORD_LENGTH+" characters long")
    private String password;
    /**
     * The Users role in the application
     *
     * @since 1.0
     */
    private String userRole = "guest";


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return userUUID.equals(user.userUUID) && userName.equals(user.userName) && password.equals(user.password) && userRole.equals(user.userRole);
    }
}
