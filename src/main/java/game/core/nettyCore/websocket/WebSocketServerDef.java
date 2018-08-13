package game.core.nettyCore.websocket;

import game.core.nettyCore.IExecutorCallBack;
import game.core.nettyCore.http.AbstractHttpMessageLogicExecutorBase;
import game.core.nettyCore.http.HttpHandlerManager;
import game.core.nettyCore.http.HttpServerDefBuilder;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;

public class WebSocketServerDef {
    protected final String name;
    public final int port;
    public final ChannelInitializer<SocketChannel> channelInitializer;
    public final AbstractHttpMessageLogicExecutorBase messageLogicExecutor;// logic
    public final HttpHandlerManager handlerManager;
    public final int maxFrameSize;
    public final int maxConnections;
    public final long clientIdleTimeout;
    public final IExecutorCallBack executorCallBack;

    public WebSocketServerDef(String name, int port, int maxFrameSize, int maxConnections, long clientIdleTimeout,
                              AbstractHttpMessageLogicExecutorBase messageLogicExecutor,
                              HttpHandlerManager handlerManager, IExecutorCallBack executorCallBack) {
        this.name = name;
        this.port = port;

        this.channelInitializer = new DefaultWebSocketChannelInstaller(this);

        this.messageLogicExecutor = messageLogicExecutor;
        this.handlerManager = handlerManager;
        this.maxFrameSize = maxFrameSize;
        this.maxConnections = maxConnections;
        this.clientIdleTimeout = clientIdleTimeout;
        this.executorCallBack = executorCallBack;
    }

    public static HttpServerDefBuilder newBuilder() {
        return new HttpServerDefBuilder();
    }

}
