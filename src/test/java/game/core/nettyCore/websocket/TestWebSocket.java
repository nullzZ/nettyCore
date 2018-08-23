package game.core.nettyCore.websocket;

import game.core.nettyCore.bootstrap.websocket.WebSocketServerBootstrap;
import game.core.nettyCore.coder.ProtocolType;
import game.core.nettyCore.serverDef.ServerDef;


public class TestWebSocket {

    public static void main(String[] args) {
        try {
             //PropertyConfigurator.configure("/Users/malei/nettyCore/src/test/resources/log4j.xml");
            //PropertyConfigurator.configure("D:\\nettyCore\\src\\test\\resources\\log4j.xml");
            ServerDef serverDef = ServerDef.newBuilder().handlerPackage("game.core.nettyCore.test")
                    .protocolType(ProtocolType.JPROTOBUFF).build();
            WebSocketServerBootstrap bootstrap = new WebSocketServerBootstrap(serverDef);
            bootstrap.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
