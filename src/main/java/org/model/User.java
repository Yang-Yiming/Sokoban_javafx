package org.model;

import java.util.ArrayList;

public class User {
    public static ArrayList<User> UserInfo = new ArrayList<>(); // 所有的用户

    private String Name;
    private String Password;
    private int LevelAt;
    private int PlayingLevel;
    private int[][] PlayingMap;

    public User(String name, String password) {
        this.Name = name;
        this.Password = password;

        UserInfo.add(this);
    }

    public String getName() {
        return Name;
    }
    public String getPassword() {
        return Password;
    }

    public void update_info(int levelAt, int PlayingLevel, MapMatrix map){
        this.LevelAt = levelAt;
        this.PlayingLevel = PlayingLevel;
        this.PlayingMap = map.getMatrix();
    }
}
