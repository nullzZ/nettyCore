package game.core.nettyCore.bootstrap.websocket;

import game.core.nettyCore.serverDef.ServerDef;
import game.core.nettyCore.websocket.DefaultWebSocketChannelInstaller;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WebSocketServerBootstrap extends WebSocketCommonServer implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(WebSocketServerBootstrap.class);

    private final ServerDef serverDef;

    public WebSocketServerBootstrap(ServerDef serverDef) {
        this.serverDef = serverDef;
        Runtime.getRuntime().addShutdownHook(new Thread(this));
    }


    @Override
    public void run() {
        logger.info("server closing...");
        close();
    }

    @Override
    public void close() {
        super.close();
    }

    public void start() throws Exception {

        super.start(serverDef.port, new DefaultWebSocketChannelInstaller(serverDef));
    }

    public void start(int bossThreads, int workThreads) throws Exception {
        start(serverDef.port, serverDef.channelInitializer, bossThreads, workThreads);
    }

}
