package org.view.net;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class Client {
    String receive(Socket socket) {
        try {
            //接收服务端发送的消息
            InputStream in = socket.getInputStream();
            byte[] buf = new byte[1024];
            int len = in.read(buf);
            String str = new String(buf, 0, len, StandardCharsets.UTF_8);
            return str;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    void send(Socket socket, String message) {
        try {
            //向服务端发送消息
            socket.getOutputStream().write(message.getBytes(StandardCharsets.UTF_8));
            socket.getOutputStream().flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void start(String serverIp, int serverPort) {
        boolean connected = false;
        while(!connected) {
            try {
                Socket socket = new Socket(serverIp, serverPort);
                System.out.println("Connect to the server " + socket.getRemoteSocketAddress());
                connected = true;

                //监听服务端发送的消息
                new Thread(() -> {
                    while (true) {
                        System.out.println("Received message from the server: " + receive(socket));
                    }
                }).start();

                while (true) {
                    //输入消息
                    byte[] buf1 = new byte[1024];
                    int len1 = System.in.read(buf1);
                    //向服务端发送消息
                    send(socket, new String(buf1, 0, len1, StandardCharsets.UTF_8));
                }
                //关闭连接
            } catch (IOException e) {
                try {
                    Thread.sleep(1000); // 等待1秒后重试
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                }
//                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        Client client = new Client();
        client.start(LocalIPAddress.getLocalIP(), 8888);   //监听IP地址192.168.202.31 的8888端口
    }
}
