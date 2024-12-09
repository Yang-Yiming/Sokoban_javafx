package org.view.net;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Scanner;
import java.nio.charset.StandardCharsets;

public class Client {
    public void start(String serverIp, int serverPort) {
        try {
            Socket socket = new Socket(serverIp, serverPort);
            System.out.println("Connected to the server " + socket.getRemoteSocketAddress());

            // 接收服务端发送的消息
            Thread receiveThread = new Thread(() -> {
                try {
                    InputStream in = socket.getInputStream();
                    byte[] buf = new byte[1024];
                    int len;
                    while ((len = in.read(buf)) != -1) {
                        String str = new String(buf, 0, len, StandardCharsets.UTF_8);
                        System.out.println("Received message from the server: " + str);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            receiveThread.start();

            // 发送消息到服务端
            Scanner scanner = new Scanner(System.in);
            while (true) {
                String message = scanner.nextLine();
                OutputStream out = socket.getOutputStream();
                out.write(message.getBytes(StandardCharsets.UTF_8));
                out.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Client client = new Client();
        client.start(LocalIPAddress.getLocalIP(), 8888);
    }
}