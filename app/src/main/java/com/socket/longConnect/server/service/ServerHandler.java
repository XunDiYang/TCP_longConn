package com.socket.longConnect.server.service;

import android.os.Bundle;
import android.os.Message;

import com.google.gson.Gson;
import com.socket.longConnect.model.CMessage;
import com.socket.longConnect.model.MsgType;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;

public class ServerHandler extends ChannelInboundHandlerAdapter {
    private NettyServerDemo serverService = NettyServerDemo.getServerService();

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
        NettyChannelMap.remove(ctx.channel());
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        super.channelRead(ctx, msg);
        Gson gson = new Gson();
        CMessage recvCMsg = gson.fromJson((String) msg, CMessage.class);

        CMessage sendCMsg = new CMessage();
        sendCMsg.setFrom(serverService.ip);
        sendCMsg.setTo(recvCMsg.getFrom());
        sendCMsg.setCode(200);

        if (serverService.connMsgCallback != null){
            serverService.getHandler().post(()->{
                serverService.connMsgCallback.onEvent(recvCMsg.getCode(),recvCMsg.getMsg(),null);
            });
        }

        if (recvCMsg.getType() == MsgType.CONNECT) {
            sendCMsg.setType(MsgType.CONNECT);
            ctx.channel().writeAndFlush(sendCMsg.toJson());
            NettyChannelMap.add(sendCMsg.getTo(), ctx.channel());
        } else if (recvCMsg.getType() == MsgType.TEXT) {
            sendCMsg.setType(MsgType.TEXT);
            Channel channel = NettyChannelMap.get(recvCMsg.getFrom());
            if (channel != null) {
                channel.isWritable();
                channel.writeAndFlush(sendCMsg.toJson()).addListener((ChannelFutureListener) future -> {
                    if (!future.isSuccess()) {
                        System.out.println("send msg to " + recvCMsg.getFrom() + " failed");
                    }
                });
            }
        } else if (recvCMsg.getType() == MsgType.PING) {
            Channel channel = NettyChannelMap.get(recvCMsg.getFrom());
            if (channel != null) {
                sendCMsg.setType(MsgType.PING);
                channel.writeAndFlush(sendCMsg.toJson());
            }
        }

        ReferenceCountUtil.release(msg);
    }
}
