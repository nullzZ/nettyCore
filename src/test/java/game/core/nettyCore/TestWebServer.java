package game.core.nettyCore;

import game.core.nettyCore.bootstrap.socket.ServerBootstrap;
import game.core.nettyCore.serverDef.ServerDef;

/**
 * @author nullzZ
 */
public class TestWebServer {

    public static void main(String[] args) {
        // 启动 Server
        try {
            //PropertyConfigurator.configure("E:\\nettyCore\\src\\test\\resources\\log4j.xml");
            int port = 9090;
            ServerDef serverDef = ServerDef.newBuilder().listen(port)
                    .handlerPackage("game.core.nettyCore.test").build();
            @SuppressWarnings("resource")
            ServerBootstrap server = new ServerBootstrap(serverDef);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        server.start();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // public static void main(String[] args) {
    // HandlerManager m = new HandlerManager();
    // try {
    // m.init("game.core.nettyCore.test");
    // // System.out.println(m.getHandler((short) 10001).message());
    // } catch (Exception e) {
    // e.printStackTrace();
    // }
    // }
}
