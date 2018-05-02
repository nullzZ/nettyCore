package game.core.nettyCore.test;

import game.core.nettyCore.HandlerAnnotation;
import game.core.nettyCore.IResultHandler;
import game.core.nettyCore.message.TestWebRequest;
import game.core.nettyCore.message.TestWebResponse;
import io.netty.channel.ChannelHandlerContext;

/**
 * @author nullzZ
 */
@HandlerAnnotation(id = 10001, messageClass = TestWebRequest.class)
public class TestHandler implements IResultHandler<ChannelHandlerContext, TestWebRequest, TestWebResponse> {

    @Override
    public TestWebResponse execute(ChannelHandlerContext role, TestWebRequest message) throws Exception {
        System.out.println("res:" + message.getId());
        TestWebResponse res = new TestWebResponse();
        res.setCmd(10001);
        res.setRet("1");
        return res;
    }

}
