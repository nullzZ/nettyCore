package game.core.nettyCore.bootstrap.websocket;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WebSocketCommonServer implements java.io.Closeable {

    private static final Logger logger = LoggerFactory.getLogger(WebSocketCommonServer.class);

    public int bossThreadSize = 1;
    public int workerThreadSize = Runtime.getRuntime().availableProcessors() * 2;
    private NioEventLoopGroup bossGroup;
    private NioEventLoopGroup workerGroup;
    private ChannelFuture f;

    public void start(int port, ChannelInitializer<SocketChannel> channelInitializer) throws Exception {
        start(port, channelInitializer, bossThreadSize, workerThreadSize);
    }

    public void start(int port, ChannelInitializer<SocketChannel> channelInitializer, int bossThreads, int workThreads)
            throws Exception {
        try {
            ServerBootstrap b = new ServerBootstrap();
            bossGroup = new NioEventLoopGroup(bossThreads, r -> {
                return new Thread(r, "netty_boss");
            });
            workerGroup = new NioEventLoopGroup(workThreads, r -> {
                return new Thread(r, "netty_worker");
            });
            b.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class).childHandler(channelInitializer);
//            .option(ChannelOption.SO_BACKLOG, 128).option(ChannelOption.TCP_NODELAY, true)
//                    .option(ChannelOption.SO_KEEPALIVE, true).option(ChannelOption.SO_REUSEADDR, true)

            b.handler(new LoggingHandler(LogLevel.INFO));

            f = b.bind(port).sync();
            logger.info("webSocketServer started and listen on port:" + port);
            f.channel().closeFuture().sync();
        } finally {
            close_();
        }
    }

    private void close_() {
        logger.info("**** try shutdown NioEventLoopGroups.");
        try {
            bossGroup.shutdownGracefully().sync();
        } catch (InterruptedException e) {
        }
        try {
            workerGroup.shutdownGracefully().sync();
        } catch (InterruptedException e) {
        }
    }

    public void close() {
        logger.info("try shutdown NioEventLoopGroups.");
        try {
            bossGroup.shutdownGracefully().sync();
        } catch (InterruptedException e) {
        }
        f.channel().close();
    }
}
