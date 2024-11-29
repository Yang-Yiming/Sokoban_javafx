package org.model;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.io.*;
import java.security.NoSuchAlgorithmException;
import java.security.MessageDigest;

public class SavingManager {

    public SavingManager() {
    }

    public static void addUser(String username, String password) throws FileNotFoundException {
        try {
            password = hash(password);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        } // 将密码哈希 防止通过文件获取

        new User(username, password); //加入到userinfo里

        // 将新用户写入文件
        save();
    }

    public static int getUser(String username, String password) {
        try {
            password = hash(password);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        } // 将密码哈希 防止通过文件获取

        for (int i = 0; i < User.UserInfo.size(); i++) {
            if (User.UserInfo.get(i).getName().equals(username)) {
                if (User.UserInfo.get(i).getPassword().equals(password))
                    return i; // 返回找到的用户
                else
                    return -2; // 密码错误
            }
        }
        return -1; // 找不到用户
    }

    public static boolean NotValidString(String str) {
        return !str.matches("[a-zA-Z0-9_]+"); //是否仅包含字母数字下划线
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

    private static String dir = "src/main/resources/savings/UserInfo.json";
    private static File file = new File(dir);

    public static void save() throws FileNotFoundException {
        String str = User.UserInfotoJSON();
        // 将str写入
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file)));
        try {
            bw.write(str);
            bw.flush();
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void read() {
        // 从resources/savings/UserInfo.json读取json
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
            StringBuilder json = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                json.append(line);
            }
            br.close();
            User.UserInfofromJSON(json.toString());
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("错误");
            alert.setHeaderText("读取文件失败");
            alert.setContentText("请检查文件是否损坏或缺失");
            alert.showAndWait();

            alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("错误");
            alert.setHeaderText("是否重置存档？");
            alert.setContentText("选择\"是\"将会将存档文件清空");
            ButtonType result = alert.showAndWait().orElse(ButtonType.CANCEL);
            if (result == ButtonType.OK) {
                try {
                    save();
                } catch (IOException e1) {
                    alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("错误");
                    alert.setHeaderText("读取文件失败");
                    alert.setContentText("请检查文件是否损坏或缺失");
                    alert.showAndWait();
                }
            }
        }
    }

}



