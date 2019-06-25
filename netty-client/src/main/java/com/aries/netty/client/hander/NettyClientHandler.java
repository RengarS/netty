package com.aries.netty.client.hander;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.*;

/**
 * Created with IntelliJ IDEA.
 * User: daozhang
 * Time: 2019/6/23
 * Description:
 */
@ChannelHandler.Sharable
public class NettyClientHandler extends SimpleChannelInboundHandler<String> {

//    private ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
//
//    private static final class Runner implements Runnable {
//        public ChannelHandlerContext ctx;
//
//        @Override
//        public void run() {
//            System.out.println("准备发送消息");
//            ctx.channel().writeAndFlush(PooledByteBufAllocator.DEFAULT.directBuffer().writeBytes("hello world!_$_$".getBytes()));
//            System.out.println("消息发送完毕");
//        }
//    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        //ctx.writeAndFlush(PooledByteBufAllocator.DEFAULT.directBuffer().writeBytes("hello world!_$_$".getBytes()));

        ctx.channel().eventLoop().scheduleAtFixedRate(() -> {
            System.out.println("准备发送消息");
            ctx.channel().writeAndFlush(PooledByteBufAllocator.DEFAULT.directBuffer().writeBytes("hello world!_$_$".getBytes()));
            System.out.println("消息发送完毕");
        }, 1, 1, TimeUnit.SECONDS);

        System.out.println("channelActive:" + Thread.currentThread().getName());
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String s) throws Exception {
//        //读取ByteBuf数据
//        ByteBuf byteBuf = (ByteBuf) msg;
//        byte[] bytes = new byte[byteBuf.readableBytes()];
//        byteBuf.readBytes(bytes);
//        String response = new String(bytes, StandardCharsets.UTF_8);
        System.out.println("response:" + s + "     ThreadName:" + Thread.currentThread().getName());
        //ctx.writeAndFlush(PooledByteBufAllocator.DEFAULT.directBuffer().writeBytes("hello world!_$_$".getBytes()));

    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        System.out.println("消息读取完毕！");
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }
}
