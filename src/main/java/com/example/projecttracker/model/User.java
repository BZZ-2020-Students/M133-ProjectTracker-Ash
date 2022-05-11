package com.example.projecttracker.model;

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

    public String getUserUUID() {
        return userUUID;
    }

    public void setUserUUID(String userUUID) {
        this.userUUID = userUUID;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserRole() {
        return userRole;
    }

    public void setUserRole(String userRole) {
        this.userRole = userRole;
    }
}
