package com.socket.longConnect.client.service;

import android.util.Log;

import com.google.gson.Gson;
import com.socket.longConnect.model.CMessage;
import com.socket.longConnect.model.ConnState;
import com.socket.longConnect.model.MsgType;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.ReferenceCountUtil;

public class ClientHandler extends ChannelInboundHandlerAdapter {
    private String TAG = "ClientHandler";
    NettyClientDemo clientService = NettyClientDemo.getClientService();

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
        clientService.closeChannel();
        clientService.updateState(ConnState.UNCONNED);
        clientService.retryConn();
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        super.userEventTriggered(ctx, evt);
        if (evt instanceof IdleStateEvent){
            IdleStateEvent idleStateEvent = (IdleStateEvent) evt;
            if(idleStateEvent.state() == IdleState.WRITER_IDLE){
//                发个心跳包
                CMessage cMessage = new CMessage();
                cMessage.setFrom(clientService.getLocalIp());
                cMessage.setTo(clientService.getServerIp());
                cMessage.setType(MsgType.PING);
                ctx.writeAndFlush(cMessage);
            }
        }
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        super.channelRead(ctx, msg);

        Gson gson = new Gson();
        CMessage recvCMsg = gson.fromJson((String) msg, CMessage.class);

        if(recvCMsg.getType() == MsgType.PING){
            Log.d(TAG, "receive ping from server");
        } else if (recvCMsg.getType() == MsgType.CONNECT) {
            if (recvCMsg.getCode() == 200){
                clientService.updateState(ConnState.CONNECTED);
                if (clientService.connMsgCallback != null){
                    clientService.handler.post(()->{
                        clientService.connMsgCallback.onEvent(200,"connect success",null);
                        clientService.connMsgCallback = null;
                    });
                }
            }else{
                clientService.closeChannel();
                clientService.updateState(ConnState.UNCONNED);
                if (clientService.connMsgCallback != null){
                    clientService.handler.post(()->{
                        clientService.connMsgCallback.onEvent(recvCMsg.getCode(),recvCMsg.getMsg(),null);
                        clientService.connMsgCallback = null;
                    });
                }
            }
        } else if (recvCMsg.getType() == MsgType.TEXT) {
            Log.d(TAG, "receive text message ");

        }

        ReferenceCountUtil.release(msg);
    }
}
