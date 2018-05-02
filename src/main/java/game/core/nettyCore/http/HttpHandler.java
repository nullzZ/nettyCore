package game.core.nettyCore.http;

import io.netty.channel.ChannelHandlerContext;

public interface HttpHandler<R extends ChannelHandlerContext, M, S> extends IHttpHandler<R, M, S> {

}
