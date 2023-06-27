package com.socket.longConnect.server.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import com.socket.longConnect.model.CMessage;
import com.socket.longConnect.model.Callback;
import com.socket.longConnect.model.ConnState;
import com.socket.longConnect.model.MsgType;

import java.net.InetSocketAddress;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import io.netty.handler.codec.string.StringDecoder;

public class NettyServerDemo extends Service {
    public static String ip = "127.0.0.1";
    public static int port = 8888;
    private static Handler handler = new Handler();

    public Callback<CMessage> connMsgCallback;

    public static NettyServerDemo serverService = new NettyServerDemo();

    @Override
    public void onCreate() {
        super.onCreate();
    }

    public static String getIp() {
        return ip;
    }

    public static void setIp(String ip) {
        serverService.ip = ip;
    }

    public static int getPort() {
        return port;
    }

    public static void setPort(int port) {
        serverService.port = port;
    }

    public static Handler getHandler() {
        return handler;
    }

    public static void setHandler(Handler handler) {
        serverService.handler = handler;
    }

    public static NettyServerDemo getServerService() {
        return serverService;
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

    public void connect(@Nullable Callback<Void> callback) {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap
                    .group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
//                    .option(ChannelOption.SO_BACKLOG, 128)
//                    .option(ChannelOption.TCP_NODELAY, true) // 不延迟，直接发送
//                    .childOption(ChannelOption.SO_KEEPALIVE, true) // 保持长连接状态
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new ObjectEncoder());
                            ch.pipeline().addLast(new ObjectDecoder(ClassResolvers.cacheDisabled(null)));
                            ch.pipeline().addLast(new ServerHandler());
                        }
                    });
            ChannelFuture cf = serverBootstrap.bind(serverService.port).sync();
            cf.addListener((ChannelFutureListener) future -> {
                        if (future.isSuccess()) {
                            Log.d("NettyServerDemo","netty server start");
                            if (callback != null) {
                                handler.post(() -> callback.onEvent(null,100, MsgType.CONNECT, "Server port " + String.valueOf(port) + ": START!", null));
                            }
                        } else {
//                            // 这里一定要关闭，不然一直重试会引发OOM
//                            future.channel().close();
                            Log.d("NettyServerDemo","netty server start failed");
                            if (callback != null) {
                                handler.post(() -> callback.onEvent(null, 400,  MsgType.CONNECT, "failed", null));
                            }
                        }
                    });
            cf.channel().closeFuture();
        } catch (Exception e) {
            e.getStackTrace();
        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }

    public void recvMsg(){

    }

}
