package game.core.nettyCore.http;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;

/**
 * @author nullzZ
 */
public interface AbstractHttpMessageLogicExecutorBase {
    void execute(IHttpHandler handler, ChannelHandlerContext ctx, FullHttpRequest req, int cmd, String token, Object msg);
}
