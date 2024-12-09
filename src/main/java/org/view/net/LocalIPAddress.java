package org.view.net;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Collections;
import java.util.Enumeration;

public class LocalIPAddress {

    public static String getLocalIP() {
        try {
            // 获取并遍历所有网络接口
            Enumeration<NetworkInterface> networks = NetworkInterface.getNetworkInterfaces();
            for (NetworkInterface network : Collections.list(networks)) {
                // 忽略虚拟网络接口和回环接口（127.0.0.1）
                if (network.isLoopback() || network.isVirtual()) continue;

                // 遍历每个网络接口的IP地址
                Enumeration<InetAddress> inetAddresses = network.getInetAddresses();
                for (InetAddress inetAddress : Collections.list(inetAddresses)) {
                    // 检查是否是IPv4地址
                    if (inetAddress instanceof java.net.Inet4Address) {
                        return inetAddress.getHostAddress();
                    }
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
        return "127.0.0.1"; // 如果没有找到IPv4地址，返回环回地址作为默认值
    }

    public static void main(String[] args) {
        System.out.println("Local IP Address: " + getLocalIP());
    }
}
