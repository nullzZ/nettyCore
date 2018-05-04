package game.core.nettyCore.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
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

import static io.netty.handler.codec.http.HttpHeaderNames.CONTENT_TYPE;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

public class MessageUtil {
    public static void sendTcpMsg(ChannelHandlerContext ctx, int cmd, Object obj) {
        Message msg = Message.newBuilder().cmd(cmd).message(obj).build();
        ctx.writeAndFlush(msg);
    }

    public static void sendTcpMsg(Channel channel, int cmd, Object obj) {
        Message msg = Message.newBuilder().cmd(cmd).message(obj).build();
        channel.writeAndFlush(msg);
    }


    public static void sendHttpResponse(ChannelHandlerContext ctx,
                                        FullHttpRequest req, HttpResponseStatus status, Object msg) throws Exception {
        ByteBuf buf = null;
        DefaultFullHttpResponse res = new DefaultFullHttpResponse(HTTP_1_1, status);
        try {
            if (msg != null) {
                buf = Unpooled.wrappedBuffer(JSON.toJSONBytes(msg, SerializerFeature.NotWriteDefaultValue));
                res.content().writeBytes(buf);
            }
            io.netty.handler.codec.http.HttpUtil.setContentLength(res, res.content().readableBytes());
            res.headers().set(CONTENT_TYPE, "application/json; charset=UTF-8");
            res.headers().set("Access-Control-Allow-Origin", "*");//!!!!!解决跨域问题
            ChannelFuture f = ctx.channel().writeAndFlush(res);
            // 如果是非Keep-Alive，关闭连接
            if (!io.netty.handler.codec.http.HttpUtil.isKeepAlive(req)) {
                f.addListener(ChannelFutureListener.CLOSE);
            }
        } catch (Exception e) {
            throw new Exception("sendHttpResponseException", e);
        } finally {
//            if (buf != null) {
//                buf.release();
//            }
        }
    }

    public static void sendHttpResponse(ChannelHandlerContext ctx, FullHttpRequest req, Object msg) throws Exception {
        sendHttpResponse(ctx, req, HttpResponseStatus.OK, msg);
    }

    //
    public static void sendHttpResponse(ChannelHandlerContext ctx, HttpResponseStatus status, Object msg) throws Exception {
        ByteBuf buf = null;

        try {
            if (msg != null) {
                buf = Unpooled.wrappedBuffer(JSON.toJSONBytes(msg, SerializerFeature.NotWriteDefaultValue));
            }
            DefaultFullHttpResponse res = new DefaultFullHttpResponse(HTTP_1_1, status, buf);
            res.headers().setInt("Content-Length", res.content().readableBytes());
            res.headers().set(CONTENT_TYPE, "application/json; charset=UTF-8");
            res.headers().set("Access-Control-Allow-Origin", "*");//!!!!!解决跨域问题
            ChannelFuture f = ctx.channel().writeAndFlush(res);
            // f.addListener(ChannelFutureListener.CLOSE);
        } catch (Exception e) {
            throw new Exception("sendHttpResponseException", e);
        }
    }

    public static void sendHttpResponse(ChannelHandlerContext ctx, Object msg) throws Exception {
        sendHttpResponse(ctx, HttpResponseStatus.OK, msg);
    }
}
