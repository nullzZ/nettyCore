package game.core.nettyCore.session;

import io.netty.channel.ChannelHandlerContext;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SessionManager {
    private static Map<ChannelHandlerContext, Session> sessions = new ConcurrentHashMap<>();

    public static void put(ChannelHandlerContext ctx, Session session) {
        sessions.put(ctx, session);
    }


    public static Session get(ChannelHandlerContext ctx) {
        return sessions.get(ctx);
    }

    public static Session remove(ChannelHandlerContext ctx) {
        return sessions.remove(ctx);
    }

    public static Collection<Session> getAll() {
        return sessions.values();
    }

    public static int size() {
        return sessions.size();
    }
}
