package game.core.nettyCore.client.handler;

import game.core.nettyCore.IHandler;
import game.core.nettyCore.HandlerAnnotation;
import game.core.nettyCore.INoResultHandler;
import game.core.nettyCore.message.TestResponse;
import io.netty.channel.ChannelHandlerContext;

/**
 * @author nullzZ
 */
@HandlerAnnotation(id = 10001, messageClass = TestResponse.class)
public class TestHandler implements INoResultHandler<ChannelHandlerContext, TestResponse> {

    @Override
    public Void execute(ChannelHandlerContext role, TestResponse message) throws Throwable {
        System.out.println("res:" + message.getRet());
        return null;
    }

}
