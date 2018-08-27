package game.core.nettyCore.client.handler;

import game.core.nettyCore.HandlerAnnotation;
import game.core.nettyCore.INoResultHandler;
import game.core.nettyCore.message.TestResponse;
import game.core.nettyCore.session.Session;

/**
 * @author nullzZ
 */
@HandlerAnnotation(id = 10001)
public class TestHandler implements INoResultHandler<Session, TestResponse> {

    @Override
    public Void execute(Session session, TestResponse message) throws Throwable {
        System.out.println("res:" + message.getRet());
        return null;
    }

}
