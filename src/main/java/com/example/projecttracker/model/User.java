package com.example.projecttracker.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class User {
    private String userUUID;
    private String userName;
    private String password;
    private String userRole;

    public User() {
    }

    public User(String userUUID, String userName, String password, String userRole) {
        this.userUUID = userUUID;
        this.userName = userName;
        this.password = password;
        this.userRole = userRole;
    }

}
