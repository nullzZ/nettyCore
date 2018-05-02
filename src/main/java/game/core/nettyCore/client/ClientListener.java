package game.core.nettyCore.client;

import io.netty.channel.ChannelHandlerContext;

/**
 * @author nullzZ
 */
public interface ClientListener {

    void onConnect(ChannelHandlerContext ctx);

    void onClose(ChannelHandlerContext ctx);

//    void sendHeart(ChannelHandlerContext ctx);
}
