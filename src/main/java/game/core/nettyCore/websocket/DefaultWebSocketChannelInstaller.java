package game.core.nettyCore.websocket;

import game.core.nettyCore.AbstractMessageLogicExecutorBase;
import game.core.nettyCore.HandlerManager;
import game.core.nettyCore.IExecutorCallBack;
import game.core.nettyCore.coder.ProtocolType;
import game.core.nettyCore.proto.IProtocolFactorySelector;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.stream.ChunkedWriteHandler;

public class DefaultWebSocketChannelInstaller extends ChannelInitializer<SocketChannel> {

    private final AbstractMessageLogicExecutorBase messageLogicExecutor;
    private final HandlerManager handlerManager;
    private final IProtocolFactorySelector protocolFactorySelector;
    private final ProtocolType protocolType;
    private IExecutorCallBack executorCallBack;

    public DefaultWebSocketChannelInstaller(HandlerManager handlerManager,
                                            IProtocolFactorySelector protocolFactorySelector,
                                            ProtocolType protocolType, IExecutorCallBack executorCallBack) {
        this.messageLogicExecutor = new WebSocketLogicExecutorBase(executorCallBack);
        this.handlerManager = handlerManager;
        this.protocolFactorySelector = protocolFactorySelector;
        this.protocolType = protocolType;
    }

    public DefaultWebSocketChannelInstaller(AbstractMessageLogicExecutorBase messageLogicExecutor,
                                            HandlerManager handlerManager,
                                            IProtocolFactorySelector protocolFactorySelector,
                                            ProtocolType protocolType) {
        this.messageLogicExecutor = messageLogicExecutor;
        this.handlerManager = handlerManager;
        this.protocolFactorySelector = protocolFactorySelector;
        this.protocolType = protocolType;
    }

    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        ChannelPipeline pipeline = socketChannel.pipeline();
//        pipeline.addLast("WebSocketMessageEncoder",
//                new WebSocketMessageEncoder(protocolType, protocolFactorySelector));
        pipeline.addLast("http-codec",
                new HttpServerCodec());//将请求和应答消息解码为HTTP消息
        pipeline.addLast("aggregator",
                new HttpObjectAggregator(65536));//将HTTP消息的多个部分合成一条完整的HTTP消息
        pipeline.addLast("http-chunked",
                new ChunkedWriteHandler());//向客户端发送HTML5文件
//        pipeline.addLast("ConnectionLimiter", serverDef.connectionLimiter);
        pipeline.addLast("handler",
                new WebSocketRevHandlerBase(messageLogicExecutor,
                        handlerManager, protocolFactorySelector,
                        protocolType));

    }
}
