package game.core.nettyCore;

import game.core.nettyCore.model.Message;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;

/**
 * @author nullzZ
 */
public interface AbstractMessageLogicExecutorBase {

    void execute(IHandler handler, ChannelHandlerContext ctx, Message msg);

}
