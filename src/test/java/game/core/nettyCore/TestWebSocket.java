package game.core.nettyCore;

import game.core.nettyCore.bootstrap.websocket.WebSocketServerBootstrap;
import game.core.nettyCore.serverDef.ServerDef;

public class TestWebSocket {

    public static void main(String[] args) {
        try {
            //PropertyConfigurator.configure("E:\\nettyCore\\src\\test\\resources\\log4j.xml");
            ServerDef serverDef = ServerDef.newBuilder().handlerPackage("game.core.nettyCore.test").build();
            WebSocketServerBootstrap bootstrap = new WebSocketServerBootstrap(serverDef);
            bootstrap.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
