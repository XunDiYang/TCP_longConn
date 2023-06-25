package com.socket.longConnect.server.service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import io.netty.channel.Channel;

public class NettyChannelMap {
    private static Map<String, Channel> map = new ConcurrentHashMap<>();

    public static void add(String clientIp, Channel channel){
        map.put(clientIp,channel);
    }

    public static Channel get(String clientIp){
        return map.get(clientIp);
    }

    public static void remove(Channel channel){
        for(Map.Entry entry:map.entrySet()){
            if(entry.getValue() == channel){
                String clientIp = (String) entry.getKey();
                map.remove(clientIp);
                System.out.println(clientIp + " leave");
                break;
            }
        }
    }
}
