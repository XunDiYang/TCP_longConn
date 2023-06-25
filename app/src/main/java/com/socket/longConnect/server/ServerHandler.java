package com.socket.longConnect.server;

import io.netty.channel.ChannelInboundHandlerAdapter;

public class ServerHandler extends ChannelInboundHandlerAdapter {
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

        Message message = new Message();
        Bundle bundle = new Bundle();
        bundle.putInt("type", recvCMsg.getType());
        bundle.putString("clientIp", recvCMsg.getFrom());
        bundle.putString("msg", recvCMsg.getMsg());
        message.setData(bundle);
        NettyServerDemo.handler.sendMessage(message);

        CMessage sendCMsg = new CMessage();
        sendCMsg.setFrom(NettyServerDemo.ip);
        sendCMsg.setTo(recvCMsg.getFrom());
        sendCMsg.setCode(200);

        if (recvCMsg.getType() == MsgType.CONNECTING) {
            sendCMsg.setType(MsgType.CONNECT_SUCCESS);
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

        }

        ReferenceCountUtil.release(msg);
    }
}
