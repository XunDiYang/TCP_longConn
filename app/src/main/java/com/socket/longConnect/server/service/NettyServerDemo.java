package com.socket.longConnect.server.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;

import androidx.annotation.Nullable;

import com.socket.longConnect.model.ConnStatus;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;

public class NettyServerDemo extends Service {
    public static String ip = "127.0.0.1";
    public static int port = 8888;
    public static Handler handler;
    public static ConnStatus status = ConnStatus.UNCONNED;


    @Override
    public void onCreate() {
        super.onCreate();
    }

    public static String getIp() {
        return ip;
    }

    public static void setIp(String ip) {
        NettyServerDemo.ip = ip;
    }

    public static int getPort() {
        return port;
    }

    public static void setPort(int port) {
        NettyServerDemo.port = port;
    }

    public static Handler getHandler() {
        return handler;
    }

    public static void setHandler(Handler handler) {
        NettyServerDemo.handler = handler;
    }

    private NettyServerBootStrap nettyServerBootStrap = new NettyServerBootStrap();

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return nettyServerBootStrap;
    }

    public static class NettyServerBootStrap extends Binder {
        public void connect() {
            EventLoopGroup bossGroup = new NioEventLoopGroup();
            EventLoopGroup workerGroup = new NioEventLoopGroup();
            try {
                ServerBootstrap serverBootstrap = new ServerBootstrap();
                serverBootstrap
                        .group(bossGroup, workerGroup)
                        .channel(NioServerSocketChannel.class)
                        .childOption(ChannelOption.SO_KEEPALIVE,true)
                        .childHandler(new ChannelInitializer<SocketChannel>() {
                            @Override
                            protected void initChannel(SocketChannel ch) throws Exception {
                                ch.pipeline().addLast(new LineBasedFrameDecoder(1024));
                                ch.pipeline().addLast(new StringDecoder());
                                ch.pipeline().addLast(new ServerHandler());
                            }
                        });
                ChannelFuture f = serverBootstrap.bind(port).sync();
                System.out.println("ServerHandler started on " + port);
                f.channel().closeFuture().sync();
            } catch (Exception e) {
                e.getStackTrace();
            } finally {
                workerGroup.shutdownGracefully();
                bossGroup.shutdownGracefully();
            }
        }
    }

}
