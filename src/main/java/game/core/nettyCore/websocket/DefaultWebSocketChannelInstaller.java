package game.core.nettyCore.websocket;

import game.core.nettyCore.handler.ConnectionLimiterHandler;
import game.core.nettyCore.http.HttpRevHandlerBase;
import game.core.nettyCore.http.HttpServerDef;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.stream.ChunkedWriteHandler;

public class DefaultWebSocketChannelInstaller extends ChannelInitializer<SocketChannel> {
    private WebSocketServerDef serverDef;
    private ConnectionLimiterHandler connectionLimiter;

    public DefaultWebSocketChannelInstaller(WebSocketServerDef serverDef) {
        this.serverDef = serverDef;
        connectionLimiter = new ConnectionLimiterHandler(serverDef.maxConnections);
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
        pipeline.addLast("ConnectionLimiter", connectionLimiter);
        pipeline.addLast("handler",
                new WebSocketRevHandlerBase(serverDef));
    }
}
