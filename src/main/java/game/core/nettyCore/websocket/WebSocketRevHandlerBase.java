package game.core.nettyCore.websocket;

import game.core.nettyCore.AbstractMessageLogicExecutorBase;
import game.core.nettyCore.HandlerManager;
import game.core.nettyCore.IHandler;
import game.core.nettyCore.coder.IMessageProtocol;
import game.core.nettyCore.coder.ProtocolType;
import game.core.nettyCore.http.webSocket.SessionManager;
import game.core.nettyCore.http.webSocket.WebSocketSession;
import game.core.nettyCore.model.Message;
import game.core.nettyCore.proto.IProtocolFactorySelector;
import game.core.nettyCore.serverDef.ServerDef;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.http.websocketx.*;
import io.netty.util.CharsetUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;

import static io.netty.handler.codec.http.HttpResponseStatus.BAD_REQUEST;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

public class WebSocketRevHandlerBase extends SimpleChannelInboundHandler<Object> {
    private static final Logger logger = LoggerFactory.getLogger(WebSocketRevHandlerBase.class);
    private WebSocketServerHandshaker handshaker;
    private static final String WEBSOCKET_PATH = "/websocket";
//    private final ServerDef serverDef;

    private final AbstractMessageLogicExecutorBase messageLogicExecutor;
    private final HandlerManager handlerManager;
    private final IProtocolFactorySelector protocolFactorySelector;
    private final ProtocolType protocolType;

    public WebSocketRevHandlerBase(AbstractMessageLogicExecutorBase messageLogicExecutor,
                                   HandlerManager handlerManager, IProtocolFactorySelector protocolFactorySelector,
                                   ProtocolType protocolType) {
        this.messageLogicExecutor = messageLogicExecutor;
        this.handlerManager = handlerManager;
        this.protocolFactorySelector = protocolFactorySelector;
        this.protocolType = protocolType;
    }

//    public WebSocketRevHandlerBase(ServerDef serverDef) {
//        this.serverDef = serverDef;
//        this.messageLogicExecutor = serverDef.messageLogicExecutor;
//        this.handlerManager = serverDef.handlerManager;
//        this.protocolFactorySelector = serverDef.protocolFactorySelector;
//        this.protocolType = serverDef.protocolType;
//    }

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
                        getWebSocketLocation(req)
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
                logger.error("非websocket请求");
                sendHttpResponse(ctx, req, new DefaultFullHttpResponse(HTTP_1_1,
                        BAD_REQUEST));
            }
        }
    }

    private static String getWebSocketLocation(HttpRequest req) {
        String location = req.headers().get(HttpHeaderNames.HOST) + WEBSOCKET_PATH;
        return "ws://" + location;
//        if (WebSocketServer.SSL) {
//            return "wss://" + location;
//        } else {
//            return "ws://" + location;
//        }
    }

    private void handleWebSocketFrame(ChannelHandlerContext ctx,
                                      WebSocketFrame frame) throws UnsupportedEncodingException {
        try {
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

            ByteBuf byteBuf = frame.content();

            if (byteBuf.readableBytes() < 2) {
                return;
            }
            int len = byteBuf.readableBytes();
            short cmd = byteBuf.readShort();
            byte[] body = new byte[len - 2];
            byteBuf.readBytes(body);

            IHandler handler = handlerManager.getHandler(cmd);
            if (handler == null) {
//            MessageUtil.sendWebSocketResponse(ctx,);
                logger.error("handler is null");
            } else {
                IMessageProtocol protocol = protocolFactorySelector.getProtocol(protocolType);
                if (protocol == null) {
                    logger.error("protocol is null");
                    return;
                }
                Message mes = Message.newBuilder().cmd(cmd)
                        .message(protocol.decode(body, handlerManager.getMessageClazz(cmd))).build();

                messageLogicExecutor.execute(handler, ctx, mes);
            }
        } catch (Exception e) {
            logger.error("网络异常", e);
        }
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
