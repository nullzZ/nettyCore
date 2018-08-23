package game.core.nettyCore.session;

import io.netty.channel.ChannelHandlerContext;

public class Session {
    private ChannelHandlerContext ctx;
    //    private Map<String, Object> Attribute = new ConcurrentHashMap<>();
    private SessionAttribute sessionAttribute;

    public Session(ChannelHandlerContext ctx) {
        this.ctx = ctx;
        sessionAttribute = new SessionAttribute();
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


}
