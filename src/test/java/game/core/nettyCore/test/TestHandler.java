package game.core.nettyCore.test;

import game.core.nettyCore.HandlerAnnotation;
import game.core.nettyCore.IResultHandler;
import game.core.nettyCore.message.TestWebRequest;
import game.core.nettyCore.message.TestWebResponse;
import game.core.nettyCore.protobuf.TestRequestPb;
import io.netty.channel.ChannelHandlerContext;

/**
 * @author nullzZ
 */
@HandlerAnnotation(id = 10001)
public class TestHandler implements IResultHandler<ChannelHandlerContext, TestRequestPb, TestWebResponse> {

    @Override
    public TestWebResponse execute(ChannelHandlerContext role, TestRequestPb message) throws Exception {
        System.out.println("res:" + message.id);
        TestWebResponse res = new TestWebResponse();
        res.setCmd(10001);
        res.setRet("1");
        return res;
    }

}
