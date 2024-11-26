package org.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

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

    @Override
    public String toString() {
        return "User{" +
                "Name='" + Name + '\'' +
                ", Password='" + Password + '\'' +
                ", LevelAt=" + LevelAt +
                ", PlayingLevel=" + PlayingLevel +
                '}';
    } // 调试用

    //JSON
    public String toJSON() {
        StringBuilder json = new StringBuilder();
        json.append("{");

        json.append("\"Name\":\"").append(Name).append("\",");
        json.append("\"Password\":\"").append(Password).append("\",");
        json.append("\"LevelAt\":").append(LevelAt).append(",");
        json.append("\"PlayingLevel\":").append(PlayingLevel).append(",");

        // playing map
        json.append("\"PlayingMap\":[");
        if (PlayingMap!= null) {
            for (int i = 0; i < PlayingMap.length; i++) {
                json.append("[");
                for (int j = 0; j < PlayingMap[i].length; j++) {
                    json.append(PlayingMap[i][j]);
                    if (j < PlayingMap[i].length - 1) {
                        json.append(",");
                    }
                }
                json.append("]");
                if (i < PlayingMap.length - 1) {
                    json.append(",");
                }
            }
        }
        json.append("]");

        json.append("}");

        return json.toString();
    }

    public static User fromJSON(String json) {
        if(json == null || json.isEmpty()) {
            return null;
        }
        // 去除所有空格和\n
        json = json.replaceAll("\\s", "");
        json = json.replaceAll("\n", "");

        // 去除首尾的大括号
        json = json.substring(1, json.length() - 1);

        // 以逗号分割字符串，得到各个属性的键值对
        String[] keyValuePairs = json.split(",");

        HashMap<String, String> jsonMap = new HashMap<>();
        for (String pair : keyValuePairs) {
            String[] keyValue = pair.split(":");
            jsonMap.put(keyValue[0].trim().substring(1, keyValue[0].trim().length() - 1), keyValue[1].trim());
        }

        String name = jsonMap.get("Name");
        String password = jsonMap.get("Password");
        int levelAt = Integer.parseInt(jsonMap.get("LevelAt"));
        int playingLevel = Integer.parseInt(jsonMap.get("PlayingLevel"));

        // 处理PlayingMap
        String playingMapStr = jsonMap.get("PlayingMap");
        int[][] playingMap = null;
        if (!playingMapStr.equals("[]") && !playingMapStr.equals("[]}")) {
            playingMapStr = playingMapStr.substring(1, playingMapStr.length() - 1);
            String[] rows = playingMapStr.split("],");
            playingMap = new int[rows.length][];
            for (int i = 0; i < rows.length; i++) {
                String rowStr = rows[i].substring(1);
                String[] elements = rowStr.split(",");
                playingMap[i] = new int[elements.length];
                for (int j = 0; j < elements.length; j++) {
                    playingMap[i][j] = Integer.parseInt(elements[j]);
                }
            }
        }

        User user = new User(name, password);
        user.LevelAt = levelAt;
        user.PlayingLevel = playingLevel;
        user.PlayingMap = playingMap;

        return user;
    }

    public static String UserInfotoJSON() {
        StringBuilder json = new StringBuilder();
        json.append("[");
        for (int i = 0; i < UserInfo.size(); i++) {
            json.append(UserInfo.get(i).toJSON());
            if (i < UserInfo.size() - 1) {
                json.append(",");
            }
        }
        json.append("]");
        return json.toString();
    }

    public static void UserInfofromJSON(String json) {
        if(json == null || json.isEmpty()) {
            return;
        }

        json = json.substring(1, json.length() - 1); // 去除[]
        String[] userStrs = json.split("},"); // 以}分割

        for (String userStr : userStrs) {
            User user = User.fromJSON(userStr + "}");
            UserInfo.add(user);
        }
    }
}
