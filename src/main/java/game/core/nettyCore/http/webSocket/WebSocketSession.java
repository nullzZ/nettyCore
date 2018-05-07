package game.core.nettyCore.http.webSocket;

import io.netty.channel.ChannelHandlerContext;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class WebSocketSession {
    private ChannelHandlerContext ctx;
    private boolean isLogin;
    private Map<String, Object> Attribute = new ConcurrentHashMap<>();

    public ChannelHandlerContext getCtx() {
        return ctx;
    }

    public void setCtx(ChannelHandlerContext ctx) {
        this.ctx = ctx;
    }

    public boolean isLogin() {
        return isLogin;
    }

    public void setLogin(boolean login) {
        isLogin = login;
    }

    public Map<String, Object> getAttribute() {
        return Attribute;
    }

    public void setAttribute(Map<String, Object> attribute) {
        Attribute = attribute;
    }
}
