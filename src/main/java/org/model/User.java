package org.model;

import java.util.ArrayList;
import java.util.HashMap;

public class User {
    public static ArrayList<User> UserInfo = new ArrayList<>(); // 所有的用户

    private String Name;
    private String Password;
    private int LevelAt = 0;
    private int LevelAtStep;
    private int MaxLevel = 0;
    private int MaxDifficultyLevel = 0;
    private int MaxTreasure = 0;
    private int MinTime = -1;
    private int Item_hintNumber = 3;
    private int Item_plusNumber = 3;
    private int Item_withdrawNumber = 3;
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
    public int getMaxTreasure() {
        return MaxTreasure;
    }
    public int getMinTime() {
        return MinTime;
    }
    public int getItem_hintNumber() {
        return Item_hintNumber;
    }
    public int getItem_plusNumber() {
        return Item_plusNumber;
    }
    public int getItem_withdrawNumber() {
        return Item_withdrawNumber;
    }
    public int getMaxDifficultyLevel() {
        return MaxDifficultyLevel;
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
    public void setMaxDifficultyLevel(int maxDifficultyLevel) {
        MaxDifficultyLevel = maxDifficultyLevel;
    }
    public void setMaxTreasure(int maxTreasure) {
        MaxTreasure = maxTreasure;
    }
    public void setMinTime(int minTime) {
        this.MinTime = minTime;
    }
    public void setItem_hintNumber(int item_hintNumber) {
        this.Item_hintNumber = item_hintNumber;
    }
    public void setItem_plusNumber(int item_plusNumber) {
        this.Item_plusNumber = item_plusNumber;
    }
    public void setItem_withdrawNumber(int item_withdrawNumber) {
        this.Item_withdrawNumber = item_withdrawNumber;
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

    public void update_info(int levelAt, int levelAtStep, int maxLevel, int maxTreasure, int minTime, int item_hintNumber, int item_plusNumber, int item_withdrawNumber, int MoveCount, InfiniteMap map){
        this.LevelAt = levelAt;
        this.LevelAtStep = levelAtStep;
        this.MaxLevel = maxLevel;
        this.MaxTreasure = maxTreasure;
        this.MinTime = minTime;
        this.Item_hintNumber = item_hintNumber;
        this.Item_plusNumber = item_plusNumber;
        this.Item_withdrawNumber = item_withdrawNumber;
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
                ", MaxDifficultyLevel=" + MaxDifficultyLevel +
                ", MaxTreasure=" + MaxTreasure +
                ", MinTime=" + MinTime +
                ", Item_hintNumber=" + Item_hintNumber +
                ", Item_plusNumber=" + Item_plusNumber +
                ", Item_withdrawNumber=" + Item_withdrawNumber +
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
        json.append("\"MaxDifficultyLevel\":").append(MaxDifficultyLevel).append(",");
        json.append("\"MaxTreasure\":").append(MaxTreasure).append(",");
        json.append("\"MinTime\":").append(MinTime).append(",");
        json.append("\"Item_hintNumber\":").append(Item_hintNumber).append(",");
        json.append("\"Item_plusNumber\":").append(Item_plusNumber).append(",");
        json.append("\"Item_withdrawNumber\":").append(Item_withdrawNumber).append(",");
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
        int MaxDifficultyLevel = Integer.parseInt(jsonMap.get("MaxDifficultyLevel"));
        int MaxTreasure = Integer.parseInt(jsonMap.get("MaxTreasure"));
        int MinTime = Integer.parseInt(jsonMap.get("MinTime"));
        int Item_hintNumber = Integer.parseInt(jsonMap.get("Item_hintNumber"));
        int Item_plusNumber = Integer.parseInt(jsonMap.get("Item_plusNumber"));
        int Item_withdrawNumber = Integer.parseInt(jsonMap.get("Item_withdrawNumber"));
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
        user.MaxDifficultyLevel = MaxDifficultyLevel;
        user.MaxTreasure = MaxTreasure;
        user.MinTime = MinTime;
        user.Item_hintNumber = Item_hintNumber;
        user.Item_plusNumber = Item_plusNumber;
        user.Item_withdrawNumber = Item_withdrawNumber;
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
