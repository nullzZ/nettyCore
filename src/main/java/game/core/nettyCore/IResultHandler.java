package game.core.nettyCore;

import io.netty.channel.ChannelHandlerContext;

public interface IResultHandler<R extends ChannelHandlerContext, M, S> extends IHandler<R, M, S> {

}
