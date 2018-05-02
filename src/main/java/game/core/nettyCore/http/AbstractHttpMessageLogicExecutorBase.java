package game.core.nettyCore.http;

import game.core.nettyCore.IHandler;
import game.core.nettyCore.model.Message;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;

/**
 * @author nullzZ
 */
public interface AbstractHttpMessageLogicExecutorBase {
    void execute(IHttpHandler handler, ChannelHandlerContext ctx, FullHttpRequest req, int cmd, String token, Object msg);
}
