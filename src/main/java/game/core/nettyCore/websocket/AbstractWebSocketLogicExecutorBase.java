package game.core.nettyCore.websocket;

import game.core.nettyCore.IHandler;
import io.netty.channel.ChannelHandlerContext;

/**
 * @author nullzZ
 */
public interface AbstractWebSocketLogicExecutorBase {
    void execute(IHandler handler, ChannelHandlerContext ctx, int cmd, Object msg);
}
