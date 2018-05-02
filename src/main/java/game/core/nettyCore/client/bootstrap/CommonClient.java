package game.core.nettyCore.client.bootstrap;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.concurrent.ThreadFactory;

public class CommonClient implements java.io.Closeable {

    private static final Logger logger = LoggerFactory.getLogger(CommonClient.class);
    public int bossThreadSize = 1;
    public int workerThreadSize = 1;
    private NioEventLoopGroup workerGroup;
    private Channel ch;
    private Bootstrap b;

    public void connect(String host, int port, ChannelInitializer<SocketChannel> channelInitializer) throws Exception {
        connect(host, port, channelInitializer, workerThreadSize, null);
    }

    public void connect(String host, int port, ChannelInitializer<SocketChannel> channelInitializer,
                        ChannelFutureListener channelFutureListener) throws Exception {
        connect(host, port, channelInitializer, workerThreadSize, channelFutureListener);
    }

    public void connect(String host, int port, ChannelInitializer<SocketChannel> channelInitializer, int workThreads,
                        ChannelFutureListener channelFutureListener) throws Exception {
        try {
            b = new Bootstrap();
            workerGroup = new NioEventLoopGroup(workThreads, new ThreadFactory() {

                public Thread newThread(Runnable r) {
                    return new Thread(r, "netty_client_worker");
                }
            });
            b.group(workerGroup).channel(NioSocketChannel.class).handler(channelInitializer)
                    .option(ChannelOption.SO_KEEPALIVE, true);
            /// b.handler(new LoggingHandler(LogLevel.INFO));

            ChannelFuture f = b.connect(new InetSocketAddress(host, port));
            ch = f.channel();
            if (channelFutureListener != null) {
                f.addListener(channelFutureListener);
            }

            logger.info("connect host:" + host + "|port:" + port);
//            f.channel().closeFuture().sync();
        } finally {
//            close_();
        }
    }

    private void close_() {
        logger.info("**** try shutdown NioEventLoopGroups.");
        try {
            workerGroup.shutdownGracefully().sync();
        } catch (InterruptedException e) {
        }
    }

    public void close() {
        logger.info("try shutdown NioEventLoopGroups.");
        try {
            workerGroup.shutdownGracefully().sync();
        } catch (InterruptedException e) {
        }
        //f.channel().close();
    }

//    public void sendMessage(Object msg) {
//        ch.writeAndFlush(msg);
//    }
}
