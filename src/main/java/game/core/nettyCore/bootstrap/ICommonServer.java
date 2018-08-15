package game.core.nettyCore.bootstrap;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;

public interface ICommonServer {

    void start(int port, ChannelInitializer<SocketChannel> channelInitializer) throws Exception;

    void start(int port, ChannelInitializer<SocketChannel> channelInitializer, int bossThreads, int workThreads) throws Exception;

    void close();
}
