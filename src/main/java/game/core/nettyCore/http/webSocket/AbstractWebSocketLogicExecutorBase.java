package game.core.nettyCore.http.webSocket;

import game.core.nettyCore.http.webSocket.IWebSocketHandler;
import io.netty.channel.ChannelHandlerContext;

/**
 * @author nullzZ
 */
public interface AbstractWebSocketLogicExecutorBase {
    void execute(IWebSocketHandler handler, ChannelHandlerContext ctx, int cmd, Object msg);
}
