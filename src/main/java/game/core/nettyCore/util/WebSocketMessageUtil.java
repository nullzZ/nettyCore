package game.core.nettyCore.util;

import game.core.nettyCore.coder.IMessageProtocol;
import game.core.nettyCore.coder.ProtocolType;
import game.core.nettyCore.proto.ProtocolFactorySelector;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;

public class WebSocketMessageUtil {
    private static final short HEAD_LEN = 2;

//    public static <T> void sendByJPB(ChannelHandlerContext ctx, short cmd, T msg) throws Exception {
//        sendByProtocolType(ctx, cmd, msg, ProtocolType.JPROTOBUFF);
//    }
//
//    public static <T> void sendByJPB(ChannelHandlerContext ctx, short cmd) throws Exception {
//        sendByProtocolType(ctx, cmd, null, ProtocolType.JPROTOBUFF);
//    }

    public static void sendByProtocolType(ChannelHandlerContext ctx, short cmd, ProtocolType protocolType) throws Exception {
        sendByProtocolType(ctx, cmd, null, protocolType);
    }

    public static <T> void sendByProtocolType(ChannelHandlerContext ctx, short cmd, T msg, ProtocolType protocolType) throws Exception {
        ByteBuf buf = null;
        try {
            int len = HEAD_LEN;
            byte[] bb = null;
            if (msg != null) {
                IMessageProtocol protocol = ProtocolFactorySelector.getInstance().getProtocol(protocolType);
                bb = protocol.encode(msg);
                len += bb.length;
            }

            buf = PooledByteBufAllocator.DEFAULT.directBuffer(len);
            buf.writeShort(cmd);
            if (msg != null && bb != null) {
                buf.writeBytes(bb);
            }
            ctx.channel().writeAndFlush(new BinaryWebSocketFrame(buf));

        } catch (Exception e) {
            throw e;
        } finally {
//            if (buf != null) {
//                buf.release();
//            }
        }
    }
}
