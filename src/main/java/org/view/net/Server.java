package org.view.net;
import javafx.application.Platform;
import javafx.scene.input.KeyCode;
import org.view.level.FightLevelManager;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class Server {
    ServerSocket serverSocket;
    public Socket socket;
    FightLevelManager fightLevelManager;
    public Server(FightLevelManager fightLevelManager){
        this.fightLevelManager = fightLevelManager;
    }
    public String receive(Socket socket) {
        try {
            byte[] buf = new byte[1024];
            int len = socket.getInputStream().read(buf);
            String str = new String(buf, 0, len, StandardCharsets.UTF_8);
            return str;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void send(Socket socket, String message) {
        try {
            socket.getOutputStream().write(message.getBytes(StandardCharsets.UTF_8));
            socket.getOutputStream().flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void start(int port, int num, String ipAddress) {
        try {
            serverSocket = new ServerSocket(port, num, InetAddress.getByName(ipAddress));
            System.out.println("Server started on " + ipAddress + ":" + port);
            //服务端循环接收客户端连接，在接收到连接后，向该客户端发送"Hello World!"
//            while (true) {
                socket = serverSocket.accept();
                System.out.println("Client " + socket.getInetAddress() + " connected.");

                //向当前客户端发送消息
//                send(socket, "Hello World!");
                //监听客户端发送的消息
                new Thread(() -> {
                    while (true) {
                        String s = receive(socket);
                        if(s.startsWith("!")){
                            Platform.runLater(() -> {
//                                System.out.println("啊啊啊让我 load");
                                fightLevelManager.button2LoadLevel(socket);
                            });
                        }else if(s.startsWith("W")){
                            Platform.runLater(() -> {
                                fightLevelManager.keyCodeEvent(KeyCode.UP);
                            });
                        }else if(s.startsWith("S")){
                            Platform.runLater(() -> {
                                fightLevelManager.keyCodeEvent(KeyCode.DOWN);
                            });
                        }else if(s.startsWith("A")){
                            Platform.runLater(() -> {
                                fightLevelManager.keyCodeEvent(KeyCode.LEFT);
                            });
                        }else if(s.startsWith("D")){
                            Platform.runLater(() -> {
                                fightLevelManager.keyCodeEvent(KeyCode.RIGHT);
                            });
                        }
//                        System.out.println("Received message from the client: " + s);
                    }
                }).start();
//                return;
                //向客户端发送消息
//                while (true) {
//                    byte[] buf = new byte[1024];
//                    int len = System.in.read(buf);
//                    send(client, new String(buf, 0, len, StandardCharsets.UTF_8));
//                }
//            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
//        Server server = new Server();
//        server.start(8888, 2, LocalIPAddress.getLocalIP());
//        //在Server这个函数中，第二个参数为backlog：指定连接请求队列的长度，如果连接请求队列已满，将拒绝新的连接请求。
//        System.out.println("qwq");
//        server.send(server.socket, "再说一遍");
    }
}


