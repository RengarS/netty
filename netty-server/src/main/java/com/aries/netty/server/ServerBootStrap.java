package com.aries.netty.server;

import com.aries.netty.common.consts.ChannelConst;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created with IntelliJ IDEA.
 * User: daozhang
 * Time: 2019/6/23
 * Description:
 */
public class ServerBootStrap {

    private EventLoopGroup boss = new NioEventLoopGroup(4, this.getFactory("netty-boss-event-loop-"));
    private EventLoopGroup worker = new NioEventLoopGroup(4, this.getFactory("netty-worker-event-loop-"));
    private ChannelHandler decoder = new StringDecoder();
    private ChannelHandler encoder = new StringEncoder();
    private ChannelHandler handler = new NettyServerHandler();


    private void bind(int... ports) {
        for (int port : ports) {

            Thread thread = new Thread(() -> {
                try {
                    ServerBootstrap bootstrap = new ServerBootstrap();
                    bootstrap.group(boss, worker)
                            .channel(NioServerSocketChannel.class)
                            .option(ChannelOption.SO_BACKLOG, 1024)
                            .childHandler(new ChannelInitializer<SocketChannel>() {
                                protected void initChannel(SocketChannel socketChannel) throws Exception {
                                    socketChannel.pipeline().addLast(new DelimiterBasedFrameDecoder(10000, ChannelConst.DELIMITER));
                                    socketChannel.pipeline().addLast(decoder);
                                    socketChannel.pipeline().addLast(encoder);
                                    socketChannel.pipeline().addLast(handler);
                                }
                            });
                    ChannelFuture future = bootstrap.bind(port).sync();
                    System.out.println("netty server started!");
                    future.channel().closeFuture().sync();
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    boss.shutdownGracefully();
                    worker.shutdownGracefully();
                }
            });

            thread.setName("boot-strap-" + port);
            thread.start();
        }

    }


    /**
     * build threadFactory
     *
     * @param prefix 线程名前缀
     * @return
     */
    private ThreadFactory getFactory(final String prefix) {

        return new ThreadFactory() {

            private AtomicInteger integer = new AtomicInteger(1);

            public Thread newThread(Runnable r) {
                Thread thread = new Thread(r);
                thread.setName(prefix + integer.getAndDecrement());
                return thread;
            }

        };
    }

    public static void main(String[] args) {
        new ServerBootStrap().bind(8899, 8898);
    }
}
