package game.core.nettyCore;

import io.netty.channel.ChannelHandlerContext;

public interface INoResultHandler<R extends ChannelHandlerContext, M> extends IHandler<R, M, Void> {


}
