package com.socket.longConnect.client.service;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;

import androidx.annotation.Nullable;

import com.socket.longConnect.model.ConnStatus;

public class NettyClientDemo extends Service {
    public static String localIp = "127.0.0.1";
    public static String serverIp = "127.0.0.1";
    public static int serverPort = 8888;
    public static ConnStatus status = ConnStatus.UNCONNED;
    public static Handler handler;

    public static String getLocalIp() {
        return localIp;
    }

    public static void setLocalIp(String localIp) {
        NettyClientDemo.localIp = localIp;
    }

    public static String getServerIp() {
        return serverIp;
    }

    public static void setServerIp(String serverIp) {
        NettyClientDemo.serverIp = serverIp;
    }

    public static int getServerPort() {
        return serverPort;
    }

    public static void setServerPort(int serverPort) {
        NettyClientDemo.serverPort = serverPort;
    }

    public static ConnStatus getStatus() {
        return status;
    }

    public static void setStatus(ConnStatus status) {
        NettyClientDemo.status = status;
    }

    public static Handler getHandler() {
        return handler;
    }

    public static void setHandler(Handler handler) {
        NettyClientDemo.handler = handler;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
