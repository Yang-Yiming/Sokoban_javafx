package org.model;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import static java.lang.System.exit;

public class SavingManager {
    private static ArrayList<String[]> UserInfo = new ArrayList<>();

    public static void readUsersInfo() {
        // 从文件中读取用户名和密码
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(SavingManager.class.getResourceAsStream("/savings/userinfo.txt")))) {
            String line;
            while (((line = reader.readLine()) != null)) {
                UserInfo.add(line.split(" "));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public SavingManager() {
    }

    public static void addUser(String username, String password) {
        UserInfo.add(new String[]{username, password});
        // 将新用户写入文件
        try {
            // 以追加的方式写入
            java.io.FileWriter writer = new java.io.FileWriter(SavingManager.class.getResource("/savings/userinfo.txt").getPath(), true);
            writer.write(username + " " + password + "\n");
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();}

    }

    public static int getUser(String username, String password) {
        for (int i = 0; i < UserInfo.size(); i++) {
            if (UserInfo.get(i)[0].equals(username)) {
                if (UserInfo.get(i)[1].equals(password))
                    return i; // 返回找到的用户
                else
                    return -2; // 密码错误
            }
        }
        return -1; // 找不到用户
    }

    public static boolean validString(String str) {
        return str.matches("[a-zA-Z0-9_]+"); //是否仅包含字母数字下划线
    }
}
