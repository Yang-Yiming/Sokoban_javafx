package org.view.net;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class Server {
    public void start(int port, int num, String ipAddress) {
        try {
            ServerSocket serverSocket = new ServerSocket(port, num, InetAddress.getByName(ipAddress));
            System.out.println("Server started on " + ipAddress + ":" + port);

            while (true) {
                Socket client = serverSocket.accept();
                System.out.println("Client " + client.getInetAddress() + " connected.");

                // 向当前客户端发送消息
                OutputStream out = client.getOutputStream();
                out.write("Hello World!".getBytes(StandardCharsets.UTF_8));
                out.flush();

                // 接收客户端发送的消息
                Thread clientThread = new Thread(() -> {
                    try {
                        while (true) {
                            byte[] buf = new byte[1024];
                            int len = client.getInputStream().read(buf);
                            if (len == -1) {
                                System.out.println("Client " + client.getInetAddress() + " disconnected.");
                                return;
                            }
                            String str = new String(buf, 0, len, StandardCharsets.UTF_8);
                            System.out.println("Received message from client " + client.getInetAddress() + ": " + str);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
                clientThread.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Server server = new Server();
        server.start(8888, 2, LocalIPAddress.getLocalIP());
    }
}