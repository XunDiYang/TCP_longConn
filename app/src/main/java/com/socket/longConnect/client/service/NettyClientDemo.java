package com.socket.longConnect.client.service;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;

import androidx.annotation.Nullable;

import com.socket.longConnect.model.CMessage;
import com.socket.longConnect.model.Callback;
import com.socket.longConnect.model.ConnState;

import java.net.InetSocketAddress;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.serialization.ClassResolver;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import io.netty.handler.timeout.IdleStateHandler;

public class NettyClientDemo extends Service {
    public static NettyClientDemo clientService = new NettyClientDemo();
    public static String localIp = "127.0.0.1";
    public static String serverIp = "127.0.0.1";
    public static int serverPort = 8888;
    public static ConnState state = ConnState.UNCONNED;
    private static Handler handler = new Handler();

    public Callback<CMessage> recvMsgCallback;
    public Callback<CMessage> connMsgCallback;
    private static SocketChannel socketChannel;

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

    public static ConnState getState() {
        return state;
    }

    public static void setState(ConnState state) {
        NettyClientDemo.state = state;
    }

    public void setRecvMsgCallback(Callback<CMessage> recvMsgCallback) {
        this.recvMsgCallback = recvMsgCallback;
    }

    public static Handler getHandler() {
        return handler;
    }

    public static NettyClientDemo getClientService() {
        return clientService;
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

    public void connect(@Nullable Callback<CMessage> callback) throws InterruptedException {
        if (state == ConnState.CONNECTED) {
            return;
        }

        updateState(ConnState.CONNECTING);

        NioEventLoopGroup workerGroup = new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(workerGroup);
        bootstrap.channel(NioSocketChannel.class);
        bootstrap.option(ChannelOption.SO_KEEPALIVE, true);
        bootstrap.option(ChannelOption.TCP_NODELAY, true);
        bootstrap.handler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel ch) throws Exception {
                ch.pipeline().addLast(new IdleStateHandler(0, 30, 0));
                ch.pipeline().addLast(new ObjectEncoder());
                ch.pipeline().addLast(new ObjectDecoder(ClassResolvers.cacheDisabled(null)));
                ch.pipeline().addLast(new ClientHandler());
            }
        });
//        开启客户端
        bootstrap.connect(new InetSocketAddress(clientService.getServerIp(), clientService.getServerPort()))
                .addListener((ChannelFutureListener) future -> {
                    if (future.isSuccess()) {
                        socketChannel = (SocketChannel) future.channel();
                        connMsgCallback = callback;
                        if (callback != null) {
                            handler.post(() -> callback.onEvent(200, "connect success", null));
                        }
                    } else {
                        closeChannel();
                        updateState(ConnState.UNCONNED);
                        // 这里一定要关闭，不然一直重试会引发OOM
                        future.channel().close();
                        workerGroup.shutdownGracefully();
                        if (callback != null) {
                            handler.post(() -> callback.onEvent(400, "failed", null));
                        }
                    }
                });

    }

    public void sendMsg(CMessage message, Callback<Void> callback) {

    }

    public static void updateState(ConnState state) {
        if (NettyClientDemo.state != state) {
            NettyClientDemo.state = state;
        }
    }

    public void closeChannel() {
        if (socketChannel != null) {
            socketChannel.close();
            socketChannel = null;
        }
    }

    public static void retryConn() {

    }
}
