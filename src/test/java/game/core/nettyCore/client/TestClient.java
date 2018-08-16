package game.core.nettyCore.client;

import game.core.nettyCore.client.bootstrap.ClientBootstrap;
import game.core.nettyCore.coder.ProtocolType;
import game.core.nettyCore.message.TestRequest;
import game.core.nettyCore.model.Message;

/**
 * @author nullzZ
 */
public class TestClient {

    public static void main(String[] args) {
        ClientDef def = ClientDef.newBuilder().connect("localhost", 9090).protocolType(ProtocolType.JSON)
                .handlerPackage("game.core.nettyCore.client.handler").clientIdleTimeout(2).build();
        @SuppressWarnings("resource")
        ClientBootstrap b = new ClientBootstrap(def);
        try {
            b.connect();

            Thread.sleep(2000);
            TestRequest req = new TestRequest();
            req.setId(666);
            Message msg = Message.newBuilder().cmd((short) 10001)
                    .message(req).build();

//            b.sendMessage(msg);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
