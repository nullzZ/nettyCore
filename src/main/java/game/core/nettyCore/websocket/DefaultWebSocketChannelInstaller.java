package game.core.nettyCore.websocket;

import game.core.nettyCore.serverDef.ServerDef;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.stream.ChunkedWriteHandler;

public class DefaultWebSocketChannelInstaller extends ChannelInitializer<SocketChannel> {
    private ServerDef serverDef;

    public DefaultWebSocketChannelInstaller(ServerDef serverDef) {
        this.serverDef = serverDef;
    }

    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        ChannelPipeline pipeline = socketChannel.pipeline();
        pipeline.addLast("http-codec",
                new HttpServerCodec());//将请求和应答消息解码为HTTP消息
        pipeline.addLast("aggregator",
                new HttpObjectAggregator(65536));//将HTTP消息的多个部分合成一条完整的HTTP消息
        pipeline.addLast("http-chunked",
                new ChunkedWriteHandler());//向客户端发送HTML5文件
//        pipeline.addLast("ConnectionLimiter", serverDef.connectionLimiter);
        pipeline.addLast("handler",
                new WebSocketRevHandlerBase(serverDef));
    }
}
