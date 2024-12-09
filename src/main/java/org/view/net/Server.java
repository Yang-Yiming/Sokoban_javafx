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

            //服务端循环接收客户端连接，在接收到连接后，向该客户端发送"Hello World!"
            while (true) {
                Socket client = serverSocket.accept();
                System.out.println("Client " + client.getInetAddress() + " connected.");

                //向当前客户端发送消息
                OutputStream out = client.getOutputStream();
                out.write("Hello World!".getBytes(StandardCharsets.UTF_8));
                out.flush();   //作用是强制将缓冲区的数据输出到目的地,确保数据能够及时发送
                //监听客户端发送的消息
                new Thread(() -> {
                    while (true) {
                        try {
                            byte[] buf = new byte[1024];
                            int len = client.getInputStream().read(buf);
                            String str = new String(buf, 0, len, StandardCharsets.UTF_8);
                            System.out.println("Received message from the client: " + str);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
                //向客户端发送消息
                while (true) {
                    byte[] buf = new byte[1024];
                    int len = System.in.read(buf);
                    client.getOutputStream().write(buf, 0, len);
                    client.getOutputStream().flush();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Server server = new Server();
        server.start(8888, 10, LocalIPAddress.getLocalIP());
        //服务端在IP地址192.168.202.31 上启动，并设置端口号为8888
        //在ServerSocket这个函数中，第二个参数为backlog：指定连接请求队列的长度，如果连接请求队列已满，将拒绝新的连接请求。
    }
}


