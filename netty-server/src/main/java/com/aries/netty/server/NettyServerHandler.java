package com.aries.netty.server;

import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * Created with IntelliJ IDEA.
 * User: daozhang
 * Time: 2019/6/23
 * Description:
 */
@ChannelHandler.Sharable
public class NettyServerHandler extends SimpleChannelInboundHandler<String> {

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        System.out.println("链接断开");
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String o) throws Exception {
//        ByteBuf byteBuf = (ByteBuf) o;
//        int length = byteBuf.readableBytes();
//        byte[] bytes = new byte[length];
//        byteBuf.readBytes(bytes);
//        String request = new String(bytes, StandardCharsets.UTF_8);
        System.out.println("request:" + o + "     当前线程：" + Thread.currentThread().getName());

        ctx.writeAndFlush(PooledByteBufAllocator.DEFAULT.directBuffer().writeBytes(("response:" + o + "_$_$").getBytes()));
    }


}
