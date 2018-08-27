package game.core.nettyCore.test;

import game.core.nettyCore.HandlerAnnotation;
import game.core.nettyCore.IResultHandler;
import game.core.nettyCore.message.TestWebRequest;
import game.core.nettyCore.message.TestWebResponse;
import game.core.nettyCore.protobuf.TestRequestPb;
import game.core.nettyCore.protobuf.TestResponsePb;
import game.core.nettyCore.session.Session;
import io.netty.channel.ChannelHandlerContext;

/**
 * @author nullzZ
 */
@HandlerAnnotation(id = 10001)
public class TestHandler implements IResultHandler<Session, TestRequestPb, TestResponsePb> {

    @Override
    public TestResponsePb execute(Session session, TestRequestPb message) throws Exception {
        System.out.println("res:" + message.id);
        TestResponsePb msg = new TestResponsePb();
        msg.id = 1;
        return msg;
    }

}
