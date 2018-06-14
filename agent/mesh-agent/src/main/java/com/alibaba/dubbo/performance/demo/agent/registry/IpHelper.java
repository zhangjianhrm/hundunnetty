package com.alibaba.dubbo.performance.demo.agent.registry;

import java.net.InetAddress;

public class IpHelper {

    public static String getHostIp() throws Exception {

        String ip = InetAddress.getLocalHost().getHostAddress();
//        String ip = InetAddress.getLocalHost().getHostName();

        return ip;
    }

    public static void main(String[] args) throws Exception {
        String ip=IpHelper.getHostIp();
        System.out.println(ip);
    }
}
