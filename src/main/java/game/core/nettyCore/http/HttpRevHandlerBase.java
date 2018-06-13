package game.core.nettyCore.http;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import game.core.nettyCore.http.webSocket.SessionManager;
import game.core.nettyCore.http.webSocket.WebSocketSession;
import game.core.nettyCore.util.MessageUtil;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.http.websocketx.*;
import io.netty.util.CharsetUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;

import static io.netty.handler.codec.http.HttpHeaderNames.HOST;
import static io.netty.handler.codec.http.HttpResponseStatus.BAD_REQUEST;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

public class HttpRevHandlerBase extends SimpleChannelInboundHandler<Object> {
    private static final Logger logger = LoggerFactory.getLogger(HttpRevHandlerBase.class);
    private WebSocketServerHandshaker handshaker;
    private static final String WEBSOCKET_PATH = "/websocket";
    private final HttpServerDef serverDef;
    private final AbstractHttpMessageLogicExecutorBase messageLogicExecutor;

    public HttpRevHandlerBase(HttpServerDef serverDef) {
        this.serverDef = serverDef;
        messageLogicExecutor = serverDef.messageLogicExecutor;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        try {
            // 传统的HTTP接入
            if (msg instanceof FullHttpRequest) {
                handleHttpRequest(ctx, (FullHttpRequest) msg);
            }
            // WebSocket接入
            else if (msg instanceof WebSocketFrame) {
                handleWebSocketFrame(ctx, (WebSocketFrame) msg);
            }
        } catch (Exception e) {
            logger.error("service error", e);
        }
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    private void handleHttpRequest(ChannelHandlerContext ctx,
                                   FullHttpRequest req) throws Exception {
        if (!req.decoderResult().isSuccess()) {
            sendHttpResponse(ctx, req, new DefaultFullHttpResponse(HTTP_1_1,
                    BAD_REQUEST));
        } else {
            if ("websocket".equals(req.headers().get("Upgrade"))) {//webSocket
                // 构造握手响应返回，本机测试
                WebSocketServerHandshakerFactory wsFactory = new WebSocketServerHandshakerFactory(
                        "ws://" + req.headers().get(HOST) + WEBSOCKET_PATH
                        , null, false);
                handshaker = wsFactory.newHandshaker(req);
                if (handshaker == null) {
                    WebSocketServerHandshakerFactory
                            .sendUnsupportedVersionResponse(ctx.channel());
                } else {
                    logger.debug("websocket握手成功！！！！！！！！！！");
                    handshaker.handshake(ctx.channel(), req);//把握手消息返回给客户端
                    WebSocketSession session = new WebSocketSession();
                    session.setCtx(ctx);
                    SessionManager.put(ctx, session);
                }
            } else {//正常http请求
                if (req instanceof HttpRequest) {
                    //non-get request
                    if (HttpMethod.GET.equals(req.method())) {
                        sendHttpResponse(ctx, req, new DefaultFullHttpResponse(HTTP_1_1,
                                BAD_REQUEST));
                        logger.debug("普通HTTP请求 不使用get！！！！！！！！！！");
                        return;
                    }
                }
                if (req instanceof HttpContent) {
                    //logger.debug("普通HTTP请求COTENT！！！！！！！！！！");
                    ByteBuf buf = Unpooled.copiedBuffer(req.content());
                    JSONObject jo = JSON.parseObject(buf.array(), JSONObject.class);
                    int cmd = jo.getInteger("cmd");
                    String token = jo.getString("token");
                    String dd = "{}";
                    if (jo.containsKey("data")) {
                        dd = jo.getString("data");
                    }

                    HttpRequest m = (HttpRequest) JSON.parseObject(dd, serverDef.handlerManager.getMessageClazz(cmd));
                    String d = req.headers().get("host");
                    String ip = req.headers().get("x-forwarded-for");
                    String referer = req.headers().get("referer");
                    String ua = req.headers().get("user-agent");
                    String channel = req.headers().get("channel");
                    if (StringUtils.isNotEmpty(d)) {
                        m.setDomain(d);
                    }
                    if (StringUtils.isNotEmpty(ip)) {
                        m.setIp(ip);
                    }
                    if (StringUtils.isNotEmpty(referer)) {
                        m.setReferer(referer);
                    }
                    if (StringUtils.isNotEmpty(ua)) {
                        m.setUa(ua);
                    }
                    if (StringUtils.isNotEmpty(channel)) {
                        m.setChannelId(channel);
                    }
                    IHttpHandler handler = serverDef.handlerManager.getHandler(cmd);
                    if (handler == null) {
                        MessageUtil.sendHttpResponse(ctx, req, BAD_REQUEST, null);
                        logger.error("handler is null | cmd:" + cmd);
                    } else {
                        messageLogicExecutor.execute(handler, ctx, req, cmd, token, m);
//                        messageLogicExecutor.execute(handler, ctx, req, httpRequest);
                    }
                }
            }
        }


    }

    private void handleWebSocketFrame(ChannelHandlerContext ctx,
                                      WebSocketFrame frame) throws UnsupportedEncodingException {

        // 判断是否是关闭链路的指令
        if (frame instanceof CloseWebSocketFrame) {
            handshaker.close(ctx.channel(),
                    (CloseWebSocketFrame) frame.retain());
            return;
        }
        // 判断是否是Ping消息
        if (frame instanceof PingWebSocketFrame) {
            ctx.channel().write(
                    new PongWebSocketFrame(frame.content().retain()));
            return;
        }

        if (!(frame instanceof BinaryWebSocketFrame)) {
            throw new UnsupportedOperationException(String.format(
                    "%s frame types not supported", frame.getClass().getName()));
        }

        // 返回应答消息
//        String request = ((TextWebSocketFrame) frame).text();


//        ByteBuf buf = Unpooled.copiedBuffer(frame.content());
//        JSONObject jo = JSON.parseObject(new String(buf.array(), "utf-8"));
//        int cmd = jo.getInteger("cmd");
//        String token = jo.getString("token");
//        Object m = JSON.parseObject(jo.getString("data"), serverDef.handlerManager.getMessageClazz(cmd));
//        IHttpHandler handler = serverDef.handlerManager.getHandler(cmd);
//        if (handler == null) {
////            MessageUtil.sendWebSocketResponse(ctx,);
//            logger.error("handler is null");
//        } else {
//            messageLogicExecutor.execute(handler, ctx, req, cmd, token, m);
//        }
//
//        logger.debug(String.format("%s received %s", ctx.channel(), request));
        //这里判断是否登陆

//        ctx.channel().write(
//                new TextWebSocketFrame(request
//                        + " , 欢迎使用Netty WebSocket服务，现在时刻："
//                        + new java.util.Date().toString()));
    }

    private static void sendHttpResponse(ChannelHandlerContext ctx,
                                         FullHttpRequest req, FullHttpResponse res) {

        // 返回应答给客户端
        if (res.status().code() != 200) {
            ByteBuf buf = null;
            try {
                buf = Unpooled.copiedBuffer(res.status().toString(),
                        CharsetUtil.UTF_8);
                res.content().writeBytes(buf);
                HttpUtil.setContentLength(res, res.content().readableBytes());
            } catch (Exception e) {

            } finally {
                if (buf != null) {
                    buf.release();
                }
            }

        }

        // 如果是非Keep-Alive，关闭连接
        ChannelFuture f = ctx.channel().writeAndFlush(res);
        if (!HttpUtil.isKeepAlive(req) || res.status().code() != 200) {
            f.addListener(ChannelFutureListener.CLOSE);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
            throws Exception {
        cause.printStackTrace();
        ctx.close();
    }

}
