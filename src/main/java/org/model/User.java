package org.model;

public class User {
    private String Name;
    private String Password;
    private int LevelAt;

    public User(String name, String password) {
        this.Name = name;
        this.Password = password;
    }

    public String getName() {
        return Name;
    }
    public String getPassword() {
        return Password;
    }
}
