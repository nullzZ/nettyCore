package game.core.nettyCore.websocket;

import game.core.nettyCore.websocket.handler.IWebSocketHandler;
import io.netty.channel.ChannelHandlerContext;

/**
 * @author nullzZ
 */
public interface AbstractWebSocketLogicExecutorBase {
    void execute(IWebSocketHandler handler, ChannelHandlerContext ctx, int cmd, Object msg);
}
