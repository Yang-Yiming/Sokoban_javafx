package org.model;

import java.util.ArrayList;
import java.util.HashMap;

public class User {
    public static ArrayList<User> UserInfo = new ArrayList<>(); // 所有的用户

    private String Name;
    private String Password;
    private int LevelAt;
    private int LevelAtStep;
    private int MaxLevel;
    private int MinTime = -1;
    private int MoveCount;
    private int[][] PlayingMap;

    public User(String name, String password) {
        this.Name = name;
        this.Password = password;
        if(!name.isEmpty() && !UserInfo.contains(this))
            UserInfo.add(this);
    }

    public String getName() {
        return Name;
    }
    public String getPassword() {
        return Password;
    }

    public int getLevelAt() {
        return LevelAt;
    }
    public int getLevelAtStep() {return LevelAtStep;}
    public int getMaxLevel() {
        return MaxLevel;
    }
    public int getMinTime() {
        return MinTime;
    }
    public void setLevelAt(int levelAt) {
        LevelAt = levelAt;
    }
    public void setLevelAtStep(int levelAtStep) {
        LevelAtStep = levelAtStep;
    }
    public void setMaxLevel(int maxLevel) {
        MaxLevel = maxLevel;
    }
    public void setMinTime(int minTime) {
        this.MinTime = minTime;
    }

    public int getMoveCount() {
        return MoveCount;
    }
    public void setMoveCount(int moveCount){
        this.MoveCount = moveCount;
    }
    public void addMoveCount(){
        this.MoveCount++;
    }

    public void setPlayingMap(int[][] map) {
        PlayingMap = new int[map.length][map[0].length];
        for (int i = 0; i < map.length; i++) {
            System.arraycopy(map[i], 0, PlayingMap[i], 0, map[i].length);
        }
    }
    public int[][] getPlayingMap() {
        return PlayingMap;
    }

    public void update_info(int levelAt, int levelAtStep, int maxLevel, int minTime, int MoveCount, InfiniteMap map){
        this.LevelAt = levelAt;
        this.LevelAtStep = levelAtStep;
        this.MaxLevel = maxLevel;
        this.MinTime = minTime;
        this.MoveCount = MoveCount;
        this.PlayingMap = map.getMatrix();
    }

    @Override
    public String toString() {
        return "User{" +
                "Name='" + Name + '\'' +
                ", Password='" + Password + '\'' +
                ", LevelAt=" + LevelAt +
                ", LevelAtStep=" + LevelAtStep +
                ", MaxLevel=" + MaxLevel +
                ", MinTime=" + MinTime +
                ", MoveCount=" + MoveCount +
                '}';
    } // 调试用

    //JSON
    public String toJSON() {
        StringBuilder json = new StringBuilder();
        json.append("{");

        json.append("\"Name\":\"").append(Name).append("\",");
        json.append("\"Password\":\"").append(Password).append("\",");
        json.append("\"LevelAt\":").append(LevelAt).append(",");
        json.append("\"LevelAtStep\":").append(LevelAtStep).append(",");
        json.append("\"MaxLevel\":").append(MaxLevel).append(",");
        json.append("\"MinTime\":").append(MinTime).append(",");
        json.append("\"MoveCount\":").append(MoveCount).append(",");

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
        if(json == null || json.isEmpty() || json.equals("[]")) {
            return null;
        }
        // 去除所有空格和\n
        json = json.replaceAll("\\s", "");
        json = json.replaceAll("\n", "");

        // 去除首尾的大括号
        json = json.substring(1, json.length() - 1);

        // 单独提取playingmap 防止逗号分割时出错
        int playingMapStart = json.indexOf("\"PlayingMap\":[");
        int playingMapEnd = json.lastIndexOf("]");
        String playingMapStr = json.substring(playingMapStart, playingMapEnd + 1);
        json = json.substring(0, playingMapStart) + json.substring(playingMapEnd + 1);

        // 以逗号分割字符串，得到各个属性的键值对
        String[] keyValuePairs = json.split(",");

        HashMap<String, String> jsonMap = new HashMap<>();
        for (String pair : keyValuePairs) {
            String[] keyValue = pair.split(":");
            jsonMap.put(keyValue[0].trim().substring(1, keyValue[0].trim().length() - 1), keyValue[1].trim());
        }

        String name = jsonMap.get("Name").substring(1, jsonMap.get("Name").length() - 1); // 删除""
        String password = jsonMap.get("Password").substring(1, jsonMap.get("Password").length() - 1); // 删除""
        int levelAt = Integer.parseInt(jsonMap.get("LevelAt"));
        int levelAtStep = Integer.parseInt(jsonMap.get("LevelAtStep"));
        int MaxLevel = Integer.parseInt(jsonMap.get("MaxLevel"));
        int MinTime = Integer.parseInt(jsonMap.get("MinTime"));
        int MoveCount = Integer.parseInt(jsonMap.get("MoveCount"));

        // 处理PlayingMap
        playingMapStr = playingMapStr.substring(playingMapStr.indexOf("["));
        int[][] playingMap = null;
        if (!playingMapStr.equals("[]") && !playingMapStr.equals("[]}")) {
            playingMapStr = playingMapStr.substring(1, playingMapStr.length() - 1);
            String[] rows = playingMapStr.split("],");
            rows[rows.length - 1] = rows[rows.length - 1].substring(0, rows[rows.length - 1].length() - 1);
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
        user.LevelAtStep = levelAtStep;
        user.MaxLevel = MaxLevel;
        user.MinTime = MinTime;
        user.MoveCount = MoveCount;
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
        if(json == null || json.isEmpty() || json.equals("[]")) {
            return;
        }
//        System.out.println(json);
        json = json.substring(1, json.length() - 1); // 去除[]
        String[] userStrs = json.split("},"); // 以}分割

        for (String userStr : userStrs) {
            if(userStr.charAt(userStr.length() - 1) != '}')
                userStr += "}";
//            System.out.println(userStr);
            User user = User.fromJSON(userStr); // 自动加入到UserInfo
        }
    }
}
