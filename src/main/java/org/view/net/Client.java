package org.view.net;

import javafx.application.Platform;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.VBox;
import org.data.mapdata;
import org.view.level.FightLevelManager;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class Client {
    public Socket socket;
    FightLevelManager fightLevelManager;

    public Client(FightLevelManager fightLevelManager) {
        this.fightLevelManager = fightLevelManager;
    }

    public String receive(Socket socket) {
        try {
            // 接收服务端发送的消息
            InputStream in = socket.getInputStream();
            byte[] buf = new byte[1024];
            int len = in.read(buf);
            return new String(buf, 0, len, StandardCharsets.UTF_8);
        } catch (IOException e) {
//            e.printStackTrace();
            return null;
        }
    }

    public void send(Socket socket, String message) {
        try {
            // 向服务端发送消息
            socket.getOutputStream().write(message.getBytes(StandardCharsets.UTF_8));
            socket.getOutputStream().flush();
        } catch (IOException e) {
//            e.printStackTrace();
        }
    }

    int IDtoInt(String idWithM) {
        return Integer.parseInt(idWithM.substring(1));
    }
    public boolean connected = false;
    public void start(String serverIp, int serverPort) {
        connected = false;
        while (!connected) {
            try {
                socket = new Socket(serverIp, serverPort);
                System.out.println("Connect to the server " + socket.getRemoteSocketAddress());
                connected = true;

                // 监听服务端发送的消息
                new Thread(() -> {
                    while (true) {
                        String s = receive(socket);
                        if (s.startsWith("M")) {
                            int fightLevelID = IDtoInt(s);
                            Platform.runLater(() -> {
                                fightLevelManager.FightLevelID = fightLevelID;
                                fightLevelManager.button3LoadLevel(socket);
                                send(socket, "!");
                            });
                        }else if(s.startsWith("W")){
                            Platform.runLater(() -> {
                                fightLevelManager.keyCodeEvent(KeyCode.W);
                            });
                        }else if(s.startsWith("A")){
                            Platform.runLater(() -> {
                                fightLevelManager.keyCodeEvent(KeyCode.A);
                            });
                        }else if(s.startsWith("S")){
                            Platform.runLater(() -> {
                                fightLevelManager.keyCodeEvent(KeyCode.S);
                            });
                        }else if(s.startsWith("D")){
                            Platform.runLater(() -> {
                                fightLevelManager.keyCodeEvent(KeyCode.D);
                            });
                        }else if(s.startsWith("C")){
                            Platform.runLater(() -> {
                                String message = s.substring(1);
                                fightLevelManager.showMessages(1, message);
                            });
                        }else if(s.startsWith("B")){
                            Platform.runLater(() -> {
                                //结束 client
                                try {
                                    socket.close();
                                } catch (IOException e) {
//                                    e.printStackTrace();
                                }
                                fightLevelManager.root.getChildren().remove(fightLevelManager.vbox);
                                fightLevelManager.root.getChildren().remove(fightLevelManager.restartButton);
                                fightLevelManager.root.getChildren().remove(fightLevelManager.backButton);
                                fightLevelManager.root.getChildren().remove(fightLevelManager.backButton0);
                                fightLevelManager.root.getChildren().remove(fightLevelManager.levelRoot);
                                fightLevelManager.root.getChildren().remove(fightLevelManager.waitingText);
                                fightLevelManager.root.getChildren().remove(fightLevelManager.restartButton);
                                fightLevelManager.root.getChildren().remove(fightLevelManager.settingsButton);
                                fightLevelManager.root.getChildren().remove(fightLevelManager.themeButton);
                                fightLevelManager.root.getChildren().remove(fightLevelManager.homeButton);
                                fightLevelManager.root.getChildren().remove(fightLevelManager.sendButton);
                                fightLevelManager.root.getChildren().remove(fightLevelManager.textField);
                                fightLevelManager.level.root.getChildren().clear();
                                fightLevelManager.start();
                            });
                        }else if(s.startsWith("R")){
                            int fightLevelID = IDtoInt(s);
                            Platform.runLater(() -> {
                                fightLevelManager.root.getChildren().remove(fightLevelManager.vbox);
                                fightLevelManager.root.getChildren().remove(fightLevelManager.settingsButton);
                                fightLevelManager.root.getChildren().remove(fightLevelManager.themeButton);
                                fightLevelManager.root.getChildren().remove(fightLevelManager.homeButton);
                                fightLevelManager.root.getChildren().remove(fightLevelManager.sendButton);
                                fightLevelManager.root.getChildren().remove(fightLevelManager.textField);
                                fightLevelManager.inLevel3(fightLevelManager.level, socket);
                                fightLevelManager.FightLevelID = fightLevelID;
                                fightLevelManager.level.setId(fightLevelID);
                                fightLevelManager.level.init();
                            });
                        }
//                        System.out.println("Received message from the server: " + receive(socket));
                    }
                }).start();
                return;
            } catch (IOException e) {
                try {
                    Thread.sleep(3000); // 等待3秒后重试
                } catch (InterruptedException ie) {
//                    Thread.currentThread().interrupt();
                }
            }
            System.out.println("Reconnecting...");
        }
    }

    public static void main(String[] args) {
        // Client client = new Client();
        // client.start(LocalIPAddress.getLocalIP(), 8888);   // 监听8888端口
        // client.send(client.socket, "Hello World!");
    }
}