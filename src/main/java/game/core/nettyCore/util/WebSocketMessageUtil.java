package game.core.nettyCore.util;

import com.alibaba.fastjson.JSON;
import com.baidu.bjf.remoting.protobuf.Codec;
import com.baidu.bjf.remoting.protobuf.ProtobufProxy;
import game.core.nettyCore.model.Message;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;

import static io.netty.handler.codec.http.HttpHeaderNames.CONTENT_TYPE;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

public class WebSocketMessageUtil {


    public static <T> void sendByJson(ChannelHandlerContext ctx, short cmd, T msg) throws Exception {
        ByteBuf buf = null;
        try {
            if (msg != null) {
                buf = Unpooled.wrappedBuffer(JSON.toJSONBytes(msg));
                ctx.channel().writeAndFlush(
                        new BinaryWebSocketFrame(buf));
            }
        } catch (Exception e) {
            throw e;
        }
    }

    public static void sendByJson(ChannelHandlerContext ctx, Message msg) throws Exception {
        ByteBuf buf = null;
        try {
            if (msg != null) {
                buf = Unpooled.wrappedBuffer(JSON.toJSONBytes(msg));
                ctx.channel().writeAndFlush(
                        new BinaryWebSocketFrame(buf));
            }
        } catch (Exception e) {
            throw e;
        }
    }


    public static void sendWebSocketResponse(ChannelHandlerContext ctx, ByteBuf buf) throws Exception {
        try {
            ctx.channel().writeAndFlush(
                    new BinaryWebSocketFrame(buf));
        } catch (Exception e) {
            throw e;
        }

    }

}
