package game.core.nettyCore;

import io.netty.channel.ChannelHandlerContext;

/**
 * @author nullzZ
 */
public interface IHandler<R, M, S> {

    S execute(R r, M m) throws Throwable;
}
