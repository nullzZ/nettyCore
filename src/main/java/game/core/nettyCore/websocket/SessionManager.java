package game.core.nettyCore.websocket;

import io.netty.channel.ChannelHandlerContext;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SessionManager {
    private static Map<ChannelHandlerContext, WebSocketSession> sessions = new ConcurrentHashMap<>();

    public static void put(ChannelHandlerContext ctx, WebSocketSession session) {
        sessions.put(ctx, session);
    }

    public static WebSocketSession getSession(ChannelHandlerContext ctx) {
        return sessions.get(ctx);
    }
}
