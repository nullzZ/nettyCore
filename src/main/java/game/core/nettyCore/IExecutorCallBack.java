package game.core.nettyCore;

import io.netty.channel.ChannelHandlerContext;

public interface IExecutorCallBack {

    void onConnect(ChannelHandlerContext ctx);

    void onClose(ChannelHandlerContext ctx);

    void onHandleBefor(ChannelHandlerContext ctx, Object msg);

    void onHandleAfer(ChannelHandlerContext ctx, Object msg);
}
