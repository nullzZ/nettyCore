package game.core.nettyCore.session;

import game.core.nettyCore.ServerType;
import game.core.nettyCore.coder.ProtocolType;
import game.core.nettyCore.util.WebSocketMessageUtil;
import io.netty.channel.ChannelHandlerContext;

public class Session {
    private ChannelHandlerContext ctx;
    //    private Map<String, Object> Attribute = new ConcurrentHashMap<>();
    private SessionAttribute sessionAttribute;
    private ServerType type;
    private ProtocolType protocolType;

    public Session(ChannelHandlerContext ctx, ServerType serverType, ProtocolType protocolType) {
        this.ctx = ctx;
        sessionAttribute = new SessionAttribute();
        this.type = serverType;
        this.protocolType = protocolType;
    }

    public ChannelHandlerContext getCtx() {
        return ctx;
    }

    public void setCtx(ChannelHandlerContext ctx) {
        this.ctx = ctx;
    }


    public SessionAttribute getAttribute() {
        return sessionAttribute;
    }

    public void setAttribute(SessionAttribute sessionAttribute) {
        this.sessionAttribute = sessionAttribute;
    }

    public void sendMsg(ChannelHandlerContext ctx, short cmd, ProtocolType protocolType) throws Exception {
        sendMsg(ctx, cmd, null, protocolType);
    }

    public <T> void sendMsg(ChannelHandlerContext ctx, short cmd, T msg, ProtocolType protocolType) throws Exception {
        if (type == ServerType.HTTP) {

        } else if (type == ServerType.WEBSOCKET) {
            WebSocketMessageUtil.sendByProtocolType(ctx, cmd, msg, protocolType);
        } else {

        }

    }
}
