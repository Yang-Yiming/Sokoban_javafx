package org.model;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import org.model.User;
import java.security.MessageDigest;

import static java.lang.System.exit;

public class SavingManager {
    private static ArrayList<User> UserInfo = new ArrayList<>();

    public static void readUsersInfo() { //目前只有用户名和密码， 以后还要读包括关卡的各种信息 所以也许会用json改写
        // 从文件中读取用户名和密码
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(SavingManager.class.getResourceAsStream("/savings/userinfo.txt")))) {
            String line;
            while (((line = reader.readLine()) != null)) {
                User temp = new User(line.split(" ")[0], line.split(" ")[1]);
                UserInfo.add(temp);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public SavingManager() {
    }

    public static void addUser(String username, String password) {
        try {
            password = hash(password);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        } // 将密码哈希 防止通过文件获取

        User newUser = new User(username, password);
        UserInfo.add(newUser);
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
        try {
            password = hash(password);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        } // 将密码哈希 防止通过文件获取

        for (int i = 0; i < UserInfo.size(); i++) {
            if (UserInfo.get(i).getName().equals(username)) {
                if (UserInfo.get(i).getPassword().equals(password))
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

    public static String hash(String data) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("MD5");
        byte[] dataBytes = data.getBytes();
        digest.update(dataBytes);
        byte[] hashBytes = digest.digest();
        // 将字节数组转换为十六进制字符串
        StringBuffer hexString = new StringBuffer();
        for (byte b : hashBytes) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }
        return hexString.toString();
    }
}
